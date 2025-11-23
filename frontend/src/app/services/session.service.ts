import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Session } from '@app/models/session';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private sessionSubject = new BehaviorSubject<Session | null>(null);

  session$ = this.sessionSubject.asObservable();

  setSession(session: Session) {
    this.sessionSubject.next(session);
    localStorage.setItem('session', JSON.stringify(session));
  }

  clearSession() {
    this.sessionSubject.next(null);
    localStorage.removeItem('session');
  }

  getSession(): Session | null {
    const current = this.sessionSubject.value;
    if (current) return current;

    const stored = localStorage.getItem('session');
    return stored ? (JSON.parse(stored) as Session) : null;
  }
}