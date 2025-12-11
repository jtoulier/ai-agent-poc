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

    // ✅ Generar token dinámico para romper el cache del modelo
    const nonce = crypto.randomUUID();
    console.log('[MessageService] Nonce generado:', nonce);

    // ✅ Inyectar el nonce dentro del mensaje enviado al agente
    const messageWithNonce = {
      ...message,
      content: [
        {
          type: 'text',
          text: `nonce: ${nonce}\n\n${message.content[0].text}`
        }
      ]
    };

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
          messageWithNonce, // ✅ enviar mensaje con nonce
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