import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { Run } from '@app/models/run';
import { API_ROUTES } from '@app/constants/api-routes';

@Injectable({ providedIn: 'root' })
export class RunService {
  constructor(private http: HttpClient) {}

  // Consultar un run específico
  getRun(threadId: string, runId: string): Observable<Run> {
    console.log('[RunService] Consultando estado del run:', runId);

    // 🔹 Fetch dynamic token from Python token server
    return this.http.get<{ accessToken: string }>(API_ROUTES.TOKEN_SERVER.GET_TOKEN).pipe(
      switchMap(tokenResponse => {
        const token = tokenResponse.accessToken;
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`
        });
        console.log('[RunService] Token dinámico adquirido:', token);

        return this.http.get<Run>(
          API_ROUTES.AGENT.GET_RUN_STATUS(threadId, runId),
          { headers }
        );
      })
    );
  }

  // Lanzar un nuevo run sobre un thread
  createRun(threadId: string, agentName: string): Observable<Run> {
    console.log('[RunService] Creando run para thread:', threadId, 'con agentName:', agentName);

    return this.http.get<{ accessToken: string }>(API_ROUTES.TOKEN_SERVER.GET_TOKEN).pipe(
      switchMap(tokenResponse => {
        const token = tokenResponse.accessToken;
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        });
        console.log('[RunService] Token dinámico adquirido:', token);

        // 🔹 Paso 1: Listar agentes desde Azure AI Foundry
        return this.http.get<{ object: string; data: { id: string; name: string }[] }>(
          API_ROUTES.AGENT.LIST_AGENTS,
          { headers }
        ).pipe(
          switchMap(agentResponse => {
            // 🔹 Paso 2: Buscar el agente por nombre en agentResponse.data
            const agent = agentResponse.data.find(a => a.name === agentName);
            if (!agent) {
              throw new Error(`[RunService] No se encontró agente con nombre: ${agentName}`);
            }
            console.log('[RunService] Agente encontrado:', agent);

            // 🔹 Paso 3: Crear el Run con el ID del agente
            return this.http.post<Run>(
              API_ROUTES.AGENT.CREATE_RUN(threadId),
              { assistant_id: agent.id },
              { headers }
            );
          })
        );
      })
    );
  }
}