import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { Router, RouterLink } from "@angular/router";
import { Auth } from '../../services/auth';
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

  private authService = inject(Auth);

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
  onToggleSenha(): void { this.mostrarSenha = !this.mostrarSenha; }

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

    // **AGORA USA O SERVIÇO DE AUTENTICAÇÃO**
    this.authService.login(credentials).subscribe({
      next: () => {
        console.log('Login bem-sucedido!');
        alert('Login realizado com sucesso!');
        this.isSubmitting = false;
        // Redireciona para a home após o login
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Erro ao fazer login:', err);
        // Exibe uma mensagem de erro genérica. A API pode retornar detalhes no `err.error.message`
        this.errorSenha = err.error?.message || 'Email ou senha inválidos. Tente novamente.';
        this.isSubmitting = false;
      }
    });
  }
}