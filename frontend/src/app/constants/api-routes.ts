import { environment } from '@environments/environment';

const withVersion = (url: string) => `${url}?api-version=${environment.agentAPIVersion}`;

export const API_ROUTES = {
  AGENT: {
    CREATE_THREAD: withVersion(`${environment.agentAPIURL}/threads`),
    ADD_MESSAGE: (threadId: string) =>
      withVersion(`${environment.agentAPIURL}/threads/${threadId}/messages`),
    CREATE_RUN: (threadId: string) =>
      withVersion(`${environment.agentAPIURL}/threads/${threadId}/runs`),
    GET_RUN_STATUS: (threadId: string, runId: string) =>
      withVersion(`${environment.agentAPIURL}/threads/${threadId}/runs/${runId}`),
    GET_MESSAGES: (threadId: string) =>
      withVersion(`${environment.agentAPIURL}/threads/${threadId}/messages`),
    LIST_AGENTS: withVersion(`${environment.agentAPIURL}/assistants`)
  },
  BACKEND: {
    LOGIN: `${environment.backendAPIURL}/api/relationship-managers/login`,
    SAVE_THREAD: (relationshipManagerId: string) => `${environment.backendAPIURL}/api/relationship-managers/${relationshipManagerId}`
  },
  TOKEN_SERVER: {
    GET_TOKEN: `${environment.tokenServerURL}/token`
  }
};