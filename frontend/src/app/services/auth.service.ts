import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { API_ROUTES } from '@app/constants/api-routes';
import { LoginRequest, LoginResponse, LoginError, SaveThread } from '@app/models/auth';
import { Observable, throwError, switchMap } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { SessionService } from '@app/services/session.service';
import { Session } from '@app/models/session';
import { Thread } from '@app/models/thread/thread.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(
    private http: HttpClient,
    private sessionService: SessionService
  ) {}

  login(user: string, password: string): Observable<LoginResponse> {
    console.log('[LOGIN] Iniciando login con usuario:', user);

    const body: LoginRequest = { relationshipManagerId: user, password };
    console.log('[LOGIN] Body enviado al backend:', body);

    return this.http.post<LoginResponse>(API_ROUTES.BACKEND.LOGIN, body).pipe(
      switchMap((response: LoginResponse) => {
        console.log('[LOGIN] Respuesta del backend (LoginResponse):', response);

        // 🔹 Fetch dynamic token from Python token server
        return this.http.get<{ accessToken: string }>(API_ROUTES.TOKEN_SERVER.GET_TOKEN).pipe(
          switchMap(tokenResponse => {
            const token = tokenResponse.accessToken;
            // Construir headers con el token dinámico
            const headers = new HttpHeaders({
              Authorization: `Bearer ${token}`
            });
            console.log('[LOGIN] Token dinámico adquirido:', token);
            console.log('[LOGIN] Headers construidos:', headers);

            // 1. Crear thread en agente
            console.log('[THREAD] Creando thread en agente con headers:', headers);
            return this.http.post<Thread>(API_ROUTES.AGENT.CREATE_THREAD, {}, { headers }).pipe(
              switchMap((thread: Thread) => {
                console.log('[THREAD] Respuesta del agente (Thread creado):', thread);

                // 2. Construir Session
                const session: Session = {
                  fullName: response.relationshipManagerName,
                  thread,
                  messages: [],
                  lastMessage: null
                };
                console.log('[SESSION] Session construida:', session);

                this.sessionService.setSession(session);
                console.log('[SESSION] Session guardada en SessionService');

                // 3. Persistir thread en backend con PATCH
                const saveThreadBody: SaveThread = { threadId: thread.id };
                console.log('[SAVE_THREAD] Body enviado al backend:', saveThreadBody);
                console.log('[SAVE_THREAD] URL:', API_ROUTES.BACKEND.SAVE_THREAD(response.relationshipManagerId));

                return this.http.patch<LoginResponse>(
                  API_ROUTES.BACKEND.SAVE_THREAD(response.relationshipManagerId),
                  saveThreadBody
                ).pipe(
                  tap((updatedResponse) => {
                    console.log('[SAVE_THREAD] Thread guardado en backend, respuesta:', updatedResponse);
                  }),
                  // devolvemos el LoginResponse actualizado que viene del backend
                  switchMap((updatedResponse: LoginResponse) => {
                    console.log('[FINAL] Devolviendo LoginResponse actualizado:', updatedResponse);
                    return [updatedResponse];
                  })
                );
              })
            );
          })
        );
      }),
      catchError((error: HttpErrorResponse) => {
        console.log('[ERROR] Ocurrió un error en login/flujo completo:', error);
        if (error.status === 401) {
          const loginError: LoginError = error.error as LoginError;
          console.log('[ERROR] 401 detectado, LoginError:', loginError);
          return throwError(() => loginError);
        }
        console.log('[ERROR] Otro tipo de error:', error);
        return throwError(() => error);
      })
    );
  }

  logout() {
    this.sessionService.clearSession();
  }

  getCurrentSession(): Session | null {
    return this.sessionService.getSession();
  }
}