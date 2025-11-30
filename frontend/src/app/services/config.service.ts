import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private config: any;

  load(): Promise<void> {
    return fetch('/assets/config.json')
      .then(response => response.json())
      .then(json => {
        this.config = json;
      });
  }

  get agentId(): string {
    return this.config?.agentId;
  }
}