import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';

// A interface foi baseada no seu PDF `DataWare House - BACK ES2 - SPRINT 01_02.pdf`
interface JwtResponse {
  token: string;
  email: string;
  type: string; // "Bearer"
}

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private readonly TOKEN_KEY = 'auth_token';
  // A URL base da API. Ajuste se necessário.
  private readonly API_URL = 'http://localhost:8081/api/auth';

  private router = inject(Router);
  private http = inject(HttpClient);

  // BehaviorSubject para emitir o estado de login (logado/deslogado)
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  // Método para fazer login
  login(credentials: { email: string, senha: string }): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => {
          if (response && response.token) {
            this.setToken(response.token); // Armazena o token
            this.loggedIn.next(true); // Emite que o usuário está logado
          }
        })
      );
  }

  // Salva o token no localStorage
  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  // Pega o token do localStorage
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // Verifica se existe um token
  private hasToken(): boolean {
    return !!this.getToken();
  }

  // Expõe o estado de login como um Observable
  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  // Retorna o estado de login atual de forma síncrona (útil para guards)
  isUserLoggedIn(): boolean {
    return this.hasToken();
  }

  // Faz o logout do usuário
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.loggedIn.next(false); // Emite que o usuário foi deslogado
    this.router.navigate(['/login']); // Redireciona para a página de login
  }
}