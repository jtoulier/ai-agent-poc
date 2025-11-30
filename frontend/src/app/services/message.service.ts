import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { MessageRequest, MessageResponse } from '@app/models/message';
import { API_ROUTES } from '@app/constants/api-routes';

@Injectable({ providedIn: 'root' })
export class MessageService {
  constructor(private http: HttpClient) {}

  // Agregar un mensaje a un thread
  addMessageToThread(threadId: string, message: MessageRequest): Observable<MessageResponse> {
    console.log('[MessageService] Adding message to thread:', threadId, message);

    // 🔹 Fetch dynamic token from Python token server
    return this.http.get<{ accessToken: string }>(API_ROUTES.TOKEN_SERVER.GET_TOKEN).pipe(
      switchMap(tokenResponse => {
        const token = tokenResponse.accessToken;
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        });
        console.log('[MessageService] Token dinámico adquirido:', token);

        return this.http.post<MessageResponse>(
          API_ROUTES.AGENT.ADD_MESSAGE(threadId),
          message,
          { headers }
        );
      })
    );
  }

  // Obtener mensajes de un thread
  getThreadMessages(threadId: string): Observable<MessageResponse[]> {
    console.log('[MessageService] Getting messages from thread:', threadId);

    // 🔹 Fetch dynamic token from Python token server
    return this.http.get<{ accessToken: string }>(API_ROUTES.TOKEN_SERVER.GET_TOKEN).pipe(
      switchMap(tokenResponse => {
        const token = tokenResponse.accessToken;
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`
        });
        console.log('[MessageService] Token dinámico adquirido:', token);

        return this.http.get<MessageResponse[]>(
          API_ROUTES.AGENT.GET_MESSAGES(threadId),
          { headers }
        );
      })
    );
  }
}