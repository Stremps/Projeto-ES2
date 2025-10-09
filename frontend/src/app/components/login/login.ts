import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { Router, RouterLink } from "@angular/router";
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    CardModule, 
    RouterLink, 
    HttpClientModule
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  // ATENÇÃO: O HTML usa [(ngModel)]="nomeUsuario", então manteremos essa variável
  nomeUsuario: string = ''; 
  senhaUsuario: string = '';
  mostrarSenha = false;
  isSubmitting = false;
  errorEmail: string | null = null;
  errorSenha: string | null = null;

  constructor(private http: HttpClient, private router: Router) {}

  // ... (seus métodos de validação e onToggleSenha permanecem os mesmos) ...
  validateEmail(email: string): string | null {
    if (!email) return 'E-mail é obrigatório.';
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return 'E-mail inválido.';
    return null;
  }
  validateSenha(senha: string): string | null {
    if (!senha) return 'Senha é obrigatória.';
    // A validação de 8 caracteres é do seu front, a API pode ter outra regra.
    // A API de exemplo usa "123456", então vamos ajustar para 6.
    if (senha.length < 6) return 'Senha deve ter pelo menos 6 caracteres.';
    return null;
  }
  onToggleSenha(): void { /* ... */ }

  // ATUALIZE O MÉTODO ONSUBMIT
  onSubmit(): void {
    this.errorEmail = this.validateEmail(this.nomeUsuario);
    this.errorSenha = this.validateSenha(this.senhaUsuario);

    if (this.errorEmail || this.errorSenha) {
      return;
    }

    this.isSubmitting = true;

    // O JSON da API espera "email" e "senha", então mapeamos nossas variáveis
    const credentials = {
      email: this.nomeUsuario,
      senha: this.senhaUsuario
    };

    const apiUrl = 'http://localhost:8080/api/auth/login';

    this.http.post<any>(apiUrl, credentials).subscribe({ // Espera uma resposta do tipo "any" (ou uma interface específica)
      next: (response) => {
        console.log('Login bem-sucedido!', response);
        
        // **PARTE CRÍTICA: ARMAZENAR O TOKEN**
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          alert('Login realizado com sucesso!');
          this.isSubmitting = false;
          // Redireciona para /home conforme solicitado
          this.router.navigate(['/home']);
        } else {
          // Caso a resposta não venha como esperado
          this.errorSenha = 'Resposta de login inválida do servidor.';
          this.isSubmitting = false;
        }
      },
      error: (err) => {
        console.error('Erro ao fazer login:', err);
        this.errorSenha = err.error?.message || 'Email ou senha inválidos. Tente novamente.';
        this.isSubmitting = false;
      }
    });
  }
}