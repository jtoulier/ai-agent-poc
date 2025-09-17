import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { bootstrapApplication } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MessageService, Message } from './services/message.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {
  userInput = '';
  responses: Message[] = [];

  constructor(private titleService: Title, private messageService: MessageService) {
    this.titleService.setTitle('CreditsGPT - Gestiona tus créditos con IA');
  }

  sendMessage() {
    const text = this.userInput.trim();
    if (!text) return;

    // Mensaje usuario
    this.responses.unshift({ who: 'user', text });

    // Llamada al Service
    this.messageService.getJoke().subscribe({
      next: joke => {
        const reply = `${joke.setup}\n${joke.punchline}`;
        this.responses.unshift({ who: 'bot', text: reply });
      },
      error: () => {
        this.responses.unshift({ who: 'bot', text: 'Error al obtener chiste 😢' });
      }
    });

    // Limpiar textarea
    this.userInput = '';
    const textarea = document.querySelector('textarea');
    if (textarea) (textarea as HTMLTextAreaElement).style.height = 'auto';
  }

  handleEnter(event: Event) {
    const keyboardEvent = event as KeyboardEvent; // cast seguro aquí
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
