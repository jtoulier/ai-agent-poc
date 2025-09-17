import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { delay } from 'rxjs/operators';
import { LoginResponse } from '../models/login-response.model';

// Respuesta esperada del API de login


@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'TU_API_LOGIN_URL_AQUI'; // <- reemplaza con tu endpoint real

  constructor(private http: HttpClient) {}

  /**
   * Realiza login del usuario
   * @param user nombre de usuario
   * @param password contraseña
   * @returns Observable con el resultado del login
   */
  login(user: string, password: string): Observable<LoginResponse> {
    //return this.http.post<LoginResponse>(this.apiUrl, { user, password });
    if (user === 'demo' && password === '1234') {
      const response: LoginResponse = {
        threadId: 'asdf-1234-qwer-5678',
        fullName: 'Demo Usuario'
      };
      return of(response).pipe(delay(500)); // simula retraso de red
    }

    // simulación de error de login
    return throwError(() => ({
      error: { message: 'Usuario y/o password incorrectos' }
    })).pipe(delay(500));
  }    
}

