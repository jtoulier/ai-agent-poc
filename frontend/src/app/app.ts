import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { bootstrapApplication } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from './services/auth.service';
import { SessionService } from '@app/services/session.service';

import { MessageService } from '@app/services/message.service';
import { RunService } from '@app/services/run.service';


import { timer } from 'rxjs';
import { switchMap, filter, take, tap } from 'rxjs/operators';
import { MessageRequest } from '@app/models/message';
import { Run } from '@app/models/run';

import { environment } from '@environments/environment';

// Modelos
import { Session } from '@app/models/session';

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

  // 🔹 Fuente de verdad: solo Session
  session: Session | null = null;

  // 🔹 Chat
  userInput = '';
  responses: { role: 'user' | 'assistant'; text: string }[] = [];

  constructor(
    private titleService: Title,
    private authService: AuthService,
    private sessionService: SessionService,
    private messageService: MessageService,   // ✅ referencia agregada
    private runService: RunService            // ✅ referencia agregada
  ) {
    this.titleService.setTitle('CreditsAI - Gestiona tus créditos con IA');
    this.session = this.sessionService.getSession();
  }

  // 🔹 Getters derivados
  get fullName(): string {
    return this.session?.fullName ?? '';
  }

  get threadId(): string {
    return this.session?.thread?.id ?? '';
  }

  get loggedIn(): boolean {
    return !!this.session?.thread;
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
        this.session = this.authService.getCurrentSession();
        if (this.session) {
          // Inicializar chat
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
    this.session = null;

    // Limpiar estado local
    this.userInput = '';
    this.responses = [];
    this.loginUser = '';
    this.loginPassword = '';
    this.loginError = '';
  }

  sendMessage() {
    const text = this.userInput.trim();
    console.log('[SEND_MESSAGE] Texto ingresado:', text);

    if (!text) {
      console.log('[SEND_MESSAGE] Texto vacío, no se envía nada.');
      return;
    }
    if (!this.session?.thread?.id) {
      console.log('[SEND_MESSAGE] No existe thread en la sesión, abortando.');
      return;
    }

    // Mostrar mensaje del usuario en la UI
    console.log('[SEND_MESSAGE] Agregando mensaje del usuario a responses:', text);
    this.responses.unshift({ role: 'user', text });

    // 1. Crear mensaje en el thread
    const messageReq: MessageRequest = {
      role: 'user',
      content: [{ type: 'text', text }]
    };
    console.log('[SEND_MESSAGE] Request para addMessageToThread:', messageReq);

    this.messageService.addMessageToThread(this.session.thread.id, messageReq).pipe(
      // 2. Crear run
      switchMap((msgResponse) => {
        console.log('[SEND_MESSAGE] Mensaje agregado al thread, respuesta:', msgResponse);
        console.log('[SEND_MESSAGE] Creando run para thread:', this.session!.thread!.id, 'con assistantId:', environment.agentId);

        return this.runService.createRun(this.session!.thread!.id, environment.agentId).pipe(
          tap((run: Run) => {
            console.log('[SEND_MESSAGE] Run creado con ID:', run.id);
          })
        );
      }),
      // 3. Polling hasta que el run termine
      switchMap((run: Run) => {
        console.log('[SEND_MESSAGE] Run creado:', run);
        return timer(0, 2000).pipe(
          switchMap(() => {
            console.log('[SEND_MESSAGE] Consultando estado del run:', run.id);
            return this.runService.getRun(this.session!.thread!.id, run.id);
          }),
          tap((runStatus) => console.log('[SEND_MESSAGE] Estado actual del run:', runStatus.status)),
          filter(r => r.status === 'completed'),
          take(1)
        );
      }),
      // 4. Obtener mensajes del thread
      switchMap((completedRun) => {
        console.log('[SEND_MESSAGE] Run completado:', completedRun);
        console.log('[SEND_MESSAGE] Obteniendo mensajes del thread:', this.session!.thread!.id);
        return this.messageService.getThreadMessages(this.session!.thread!.id);
      })
    ).subscribe({
      next: (messagesResponse) => {
        console.log('[SEND_MESSAGE] Mensajes obtenidos del thread:', messagesResponse);

        // La API devuelve { object: 'list', data: [...] }
        const allMessages = (messagesResponse as any).data ?? messagesResponse;

        // Filtrar solo los del agente
        const assistantMessages = allMessages.filter((m: any) => m.role === 'assistant');

      // Tomar el más reciente (primer elemento, porque la API ordena descendente)
      const latestAssistantMsg = assistantMessages[0];

      if (latestAssistantMsg) {
        const textContent = latestAssistantMsg.content[0]?.text?.value ?? '';
        console.log('[SEND_MESSAGE] Último mensaje del agente (más reciente):', textContent);
        this.responses.unshift({ role: 'assistant', text: textContent });
      } else {
        console.log('[SEND_MESSAGE] No se encontró mensaje del agente en la respuesta.');
        }
      },
      error: (err) => {
        console.error('[SEND_MESSAGE][AGENT ERROR] Ocurrió un error en el flujo:', err);
        this.responses.unshift({
          role: 'assistant',
          text: 'Error al comunicarme con el agente.'
        });
      }
    });

    // Limpiar textarea
    this.userInput = '';
    console.log('[SEND_MESSAGE] Textarea limpiado.');
    const textarea = document.querySelector('textarea');
    if (textarea) {
      (textarea as HTMLTextAreaElement).style.height = 'auto';
      console.log('[SEND_MESSAGE] Textarea reseteado a altura automática.');
    }
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