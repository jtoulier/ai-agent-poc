import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { bootstrapApplication } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from './services/auth.service';
import { SessionService } from '@app/services/session.service';

// Modelos que se usan en esta vista
import { Session } from '@app/models/session';
import { Message } from '@app/models/message';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {
  // 🔹 Login
  loginUser = '';
  loginPassword = '';
  loginError = '';
  loggedIn = false;

  // 🔹 Info de sesión mostrada en UI
  fullName = '';
  threadId = '';

  // 🔹 Chat
  userInput = '';
  responses: { role: 'user' | 'assistant'; text: string }[] = [];

  constructor(
    private titleService: Title,
    private authService: AuthService,
    private sessionService: SessionService
  ) {
    this.titleService.setTitle('CreditsAI - Gestiona tus créditos con IA');

    // Si hay sesión almacenada, restaurar estado inicial
    const current = this.sessionService.getSession();
    if (current) {
      this.fullName = current.fullName || '';
      this.threadId = current.thread?.id || '';
      this.loggedIn = !!this.threadId;
    }
  }

  // 🔹 Login
  login() {
    this.loginError = '';
    if (!this.loginUser || !this.loginPassword) {
      this.loginError = 'Debe ingresar login y password';
      return;
    }

    this.authService.login(this.loginUser, this.loginPassword).subscribe({
      next: () => {
        const session = this.authService.getCurrentSession();
        if (session) {
          this.fullName = session.fullName;
          this.threadId = session.thread?.id || '';
          this.loggedIn = true;

          // Inicializar chat basado en session (por si en un futuro recuperas mensajes)
          this.responses = [];
          this.userInput = '';
        } else {
          this.loginError = 'Error inesperado al iniciar sesión';
        }
      },
      error: (err) => {
        if ('code' in err) {
          this.loginError = err.message;
        } else {
          this.loginError = 'Usuario y/o password incorrectos';
        }
      }
    });
  }

  // 🔹 Logoff
  logoff() {
    this.authService.logout();
    this.loggedIn = false;

    // Limpiar estado local
    this.fullName = '';
    this.threadId = '';
    this.userInput = '';
    this.responses = [];
    this.loginUser = '';
    this.loginPassword = '';
    this.loginError = '';
  }

  // 🔹 Chat (placeholder sin llamadas externas)
  sendMessage() {
    const text = this.userInput.trim();
    if (!text) return;

    // Mensaje usuario
    this.responses.unshift({ role: 'user', text });

    // Placeholder de respuesta del asistente
    this.responses.unshift({ role: 'assistant', text: 'Recibí tu mensaje. Pronto te responderé.' });

    // Limpiar textarea
    this.userInput = '';
    const textarea = document.querySelector('textarea');
    if (textarea) (textarea as HTMLTextAreaElement).style.height = 'auto';
  }

  handleEnter(event: Event) {
    const keyboardEvent = event as KeyboardEvent;
    if (keyboardEvent.key === 'Enter' && !keyboardEvent.shiftKey) {
      keyboardEvent.preventDefault();
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