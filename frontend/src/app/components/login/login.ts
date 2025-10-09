import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { Router, RouterLink } from "@angular/router";
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login', // Alterado para corresponder ao componente de login
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    CardModule, 
    RouterLink, 
    HttpClientModule
  ],
  templateUrl: './login.html', // Aponta para o seu HTML de login
  styleUrls: ['./login.css']    // Mude para o seu CSS de login, se houver
})
export class Login {

  // Propriedades para o formulário de login
  nomeUsuario: string = ''; // Vinculado ao campo de email no HTML
  senhaUsuario: string = ''; // Vinculado ao campo de senha no HTML
  mostrarSenha = false;
  isSubmitting = false;

  // Propriedades para exibir erros de validação
  errorEmail: string | null = null;
  errorSenha: string | null = null;

  // Injetando HttpClient para chamadas API e Router para navegação
  constructor(private http: HttpClient, private router: Router) {}

  // --- MÉTODOS ---

  // Validações dos campos de login
  validateEmail(email: string): string | null {
    if (!email) return 'E-mail é obrigatório.';
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return 'E-mail inválido.';
    return null;
  }

  validateSenha(senha: string): string | null {
    if (!senha) return 'Senha é obrigatória.';
    if (senha.length < 8) return 'Senha deve ter pelo menos 8 caracteres.';
    return null;
  }
  
  // Alterna a visibilidade da senha
  onToggleSenha(): void {
    const senhaInput = document.getElementById('senha') as HTMLInputElement;
    this.mostrarSenha = !this.mostrarSenha;
    senhaInput.type = this.mostrarSenha ? 'text' : 'password';
  }

  // Método chamado ao submeter o formulário
  onSubmit(): void {
    // Limpa erros antigos e executa novas validações
    this.errorEmail = this.validateEmail(this.nomeUsuario);
    this.errorSenha = this.validateSenha(this.senhaUsuario);

    // Se houver erros de validação, interrompe a submissão
    if (this.errorEmail || this.errorSenha) {
      return;
    }

    this.isSubmitting = true;

    const credentials = {
      email: this.nomeUsuario,
      senha: this.senhaUsuario
    };

    // Defina a URL da sua API de login
    const apiUrl = 'https://sua-api.com/login'; // <-- SUBSTITUA PELA URL REAL DA SUA API

    // Faz a chamada POST para a API de autenticação
    this.http.post(apiUrl, credentials).subscribe({
      next: (response: any) => {
        console.log('Login bem-sucedido!', response);
        alert('Login realizado com sucesso!');
        
        // Exemplo: Armazenar o token de autenticação recebido da API
        // localStorage.setItem('authToken', response.token); 
        
        this.isSubmitting = false;
        
        // Redireciona o usuário para uma página principal ou dashboard após o login
        this.router.navigate(['/home']); // Mude '/home' para a rota desejada
      },
      error: (err) => {
        console.error('Erro ao fazer login:', err);
        
        // Exibe uma mensagem de erro genérica ou específica da API
        this.errorSenha = err.error?.message || 'Email ou senha inválidos. Tente novamente.';
        this.isSubmitting = false;
      }
    });
  }
}