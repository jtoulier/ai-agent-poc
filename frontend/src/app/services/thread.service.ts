import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Thread } from '@app/models/thread';

@Injectable({ providedIn: 'root' })
export class ThreadService {
  private readonly apiUrl = '/api';

  constructor(private http: HttpClient) {}

  // Crear un nuevo thread
  createThread(): Observable<Thread> {
    return this.http.post<Thread>(`${this.apiUrl}/threads?api-version=v1`, {});
  }

  // Obtener un thread por ID
  getThread(threadId: string): Observable<Thread> {
    return this.http.get<Thread>(`${this.apiUrl}/threads/${threadId}?api-version=v1`);
  }
}