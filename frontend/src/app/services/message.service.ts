import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MessageRequest, MessageResponse } from '@app/models/message';

@Injectable({ providedIn: 'root' })
export class MessageService {
  private readonly apiUrl = '/api'; // ajusta según tu backend o environment

  constructor(private http: HttpClient) {}

  // Agregar un mensaje a un thread
  addMessageToThread(threadId: string, message: MessageRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(
      `${this.apiUrl}/threads/${threadId}/messages?api-version=v1`,
      message
    );
  }

  // Obtener mensajes de un thread
  getThreadMessages(threadId: string): Observable<MessageResponse[]> {
    return this.http.get<MessageResponse[]>(
      `${this.apiUrl}/threads/${threadId}/messages?api-version=v1`
    );
  }
}