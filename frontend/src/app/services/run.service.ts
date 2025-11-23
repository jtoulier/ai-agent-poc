import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Run } from '@app/models/run';

@Injectable({ providedIn: 'root' })
export class RunService {
  private readonly apiUrl = '/api';

  constructor(private http: HttpClient) {}

  // Consultar un run específico
  getRun(runId: string): Observable<Run> {
    return this.http.get<Run>(`${this.apiUrl}/runs/${runId}?api-version=v1`);
  }

  // Lanzar un nuevo run sobre un thread
  createRun(threadId: string, assistantId: string): Observable<Run> {
    return this.http.post<Run>(
      `${this.apiUrl}/threads/${threadId}/runs?api-version=v1`,
      { assistant_id: assistantId }
    );
  }
}