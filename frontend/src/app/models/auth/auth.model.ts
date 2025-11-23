// Request body para login
export interface LoginRequest {
  relationshipManagerId: string;
  password: string;
}

// Respuesta exitosa (200)
export interface LoginResponse {
  relationshipManagerId: string;
  relationshipManagerName: string;
  threadId: string;
  writtenAt: string; // ISO date string
}

// Respuesta de error (401)
export interface LoginError {
  message: string;
  code: string;
}

export interface SaveThread {
  threadId: string;
}