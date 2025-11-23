import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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
    const body: LoginRequest = { relationshipManagerId: user, password };

    return this.http.post<LoginResponse>(API_ROUTES.BACKEND.LOGIN, body).pipe(
      switchMap((response: LoginResponse) => {
        console.log('Login exitoso');
        // 1. Crear thread en agente
        return this.http.post<Thread>(API_ROUTES.AGENT.CREATE_THREAD, {}).pipe(
          switchMap((thread: Thread) => {
            // 2. Construir Session
            const session: Session = {
              fullName: response.relationshipManagerName,
              thread,
              messages: [],
              lastMessage: null
            };
            this.sessionService.setSession(session);

            // 3. Persistir thread en backend
            const saveThreadBody: SaveThread = { threadId: thread.id };
            return this.http.post<void>(
              API_ROUTES.BACKEND.SAVE_THREAD(response.relationshipManagerId),
              saveThreadBody
            ).pipe(
              tap(() => console.log('Thread guardado en backend')),
              // devolvemos el LoginResponse original
              switchMap(() => [response])
            );
          })
        );
      }),
      catchError((error: HttpErrorResponse) => {
        console.log(error)
        if (error.status === 401) {
          const loginError: LoginError = error.error as LoginError;
          return throwError(() => loginError);
        }
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