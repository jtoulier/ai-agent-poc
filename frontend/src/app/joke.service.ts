import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class JokeService {
  private apiUrl = 'https://official-joke-api.appspot.com/random_joke';

  constructor(private http: HttpClient) {}

  getJoke(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }
}
