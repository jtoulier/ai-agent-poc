import { Component } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface Message {
  who: 'user' | 'bot';
  text: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  template: `
    <div class="app-container">
      <!-- Header -->
      <header class="app-header">
        <div class="logo">Credits GPT</div>
      </header>

      <!-- Mensajes -->
      <main class="responses">
        <div *ngFor="let msg of responses" class="response" [ngClass]="msg.who">
          <span>{{ msg.text }}</span>
        </div>
      </main>

      <!-- Caja de entrada -->
      <footer class="input-box">
        <textarea
          #msgInput
          [(ngModel)]="userInput"
          (input)="autoResize(msgInput)"
          (keydown.enter)="handleEnter($event)"
          placeholder="Escribe tu mensaje..."></textarea>
        <button (click)="sendMessage()">➤</button>
      </footer>
    </div>
  `,
  styleUrls: ['./app.scss']
})
export class App {
  userInput: string = '';
  responses: Message[] = [];

  constructor(private http: HttpClient) {}

  sendMessage() {
    const text = this.userInput.trim();
    if (!text) return;

    // Mensaje del usuario
    this.responses.unshift({ who: 'user', text });

    // Llamada al API de chistes
    this.http.get<any>('https://official-joke-api.appspot.com/random_joke')
      .subscribe({
        next: (joke) => {
          const reply = `${joke.setup}\n${joke.punchline}`;
          this.responses.unshift({ who: 'bot', text: reply });
        },
        error: () => {
          this.responses.unshift({ who: 'bot', text: 'Error al obtener chiste 😢' });
        }
      });

    this.userInput = '';

    // 🔑 Reinicia altura del textarea al tamaño original
    const textarea = document.querySelector('textarea');
    if (textarea) {
      (textarea as HTMLTextAreaElement).style.height = 'auto';
    }    
  }

  handleEnter(event: any) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  autoResize(textarea: HTMLTextAreaElement) {
    if (!textarea) return;

    textarea.style.overflow = 'hidden';
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }
}

bootstrapApplication(App).catch(err => console.error(err));
