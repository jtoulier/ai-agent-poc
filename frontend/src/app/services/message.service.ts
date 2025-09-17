// src/app/services/message.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Message {
  who: 'user' | 'bot';
  text: string;
}

@Injectable({
  providedIn: 'root' // inyectable en toda la app
})
export class MessageService {
  constructor(private http: HttpClient) {}

  // Llamada al API de chistes (puedes reemplazar por otros servicios)
  getJoke(): Observable<{ setup: string; punchline: string }> {
    return this.http.get<{ setup: string; punchline: string }>(
      'https://official-joke-api.appspot.com/random_joke'
    );
  }

  // Método genérico que podrías usar para otros APIs
  sendMessageToApi(userMessage: string): Observable<{ reply: string }> {
    // Ejemplo, reemplaza con tu API real
    return new Observable(observer => {
      // Lógica de mock
      setTimeout(() => {
        observer.next({ reply: `Eco: ${userMessage}` });
        observer.complete();
      }, 500);
    });
  }
}
