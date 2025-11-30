import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Run } from '@app/models/run';
import { API_ROUTES } from '@app/constants/api-routes';
import { environment } from '@environments/environment';

@Injectable({ providedIn: 'root' })
export class RunService {
  constructor(private http: HttpClient) {}

  // Consultar un run específico
  getRun(threadId: string, runId: string): Observable<Run> {
    console.log('[RunService] Consultando estado del run:', runId);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${environment.agentAPIToken}`
    });

    return this.http.get<Run>(
      API_ROUTES.AGENT.GET_RUN_STATUS(threadId, runId),
      { headers }
    );
  }

  // Lanzar un nuevo run sobre un thread
  createRun(threadId: string, assistantId: string): Observable<Run> {
    console.log('[RunService] Creando run para thread:', threadId, 'con assistantId:', assistantId);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${environment.agentAPIToken}`,
      'Content-Type': 'application/json'
    });

    return this.http.post<Run>(
      API_ROUTES.AGENT.CREATE_RUN(threadId),
      { assistant_id: assistantId },
      { headers }
    );
  }
}