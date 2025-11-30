import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MessageRequest, MessageResponse } from '@app/models/message';
import { API_ROUTES } from '@app/constants/api-routes';
import { environment } from '@environments/environment';

@Injectable({ providedIn: 'root' })
export class MessageService {
  constructor(private http: HttpClient) {}

  // Agregar un mensaje a un thread
  addMessageToThread(threadId: string, message: MessageRequest): Observable<MessageResponse> {
    console.log('[MessageService] Adding message to thread:', threadId, message);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${environment.agentAPIToken}`,
      'Content-Type': 'application/json'
    });

    return this.http.post<MessageResponse>(
      API_ROUTES.AGENT.ADD_MESSAGE(threadId),
      message,
      { headers }
    );
  }

  // Obtener mensajes de un thread
  getThreadMessages(threadId: string): Observable<MessageResponse[]> {
    console.log('[MessageService] Getting messages from thread:', threadId);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${environment.agentAPIToken}`
    });

    return this.http.get<MessageResponse[]>(
      API_ROUTES.AGENT.GET_MESSAGES(threadId),
      { headers }
    );
  }
}