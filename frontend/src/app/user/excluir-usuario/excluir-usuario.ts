import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Necessário para ngModel
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-excluir-usuario',
  standalone: true,
  // Adicionamos FormsModule e HttpClientModule
  imports: [CommonModule, FormsModule, HttpClientModule], 
  templateUrl: './excluir-usuario.html',
  styleUrl: './excluir-usuario.css'
})
export class ExcluirUsuario {
  constructor(private router: Router, private http: HttpClient) {}

  // MUDANÇA: Agora o campo espera a palavra 'DELETE' para confirmação
  confirmationText: string = '';
  isLoading: boolean = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  
  private readonly deleteAccountApiUrl = 'http://localhost:8081/api/user/delete'; 

  /**
   * Método para tentar excluir a conta.
   * Agora valida se o texto digitado é 'DELETE'.
   */
  deleteAccount(): void {
    if (this.confirmationText.toUpperCase() !== 'DELETE') {
      this.errorMessage = 'A confirmação deve ser exatamente "DELETE" (sem aspas).';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;
    this.successMessage = null;

    // Em uma exclusão real, você enviaria apenas o sinal de que o usuário autenticado quer excluir.
    // Se o backend exige o texto de confirmação, você o envia.
    const payload = {
      confirmationText: this.confirmationText
    };

    // Requisição HTTP DELETE
    this.http.delete(this.deleteAccountApiUrl, { body: payload }).subscribe({
      next: (response) => {
        this.successMessage = 'Conta excluída com sucesso! Você será desconectado.';
        console.log('Conta excluída:', response);
        this.isLoading = false;
        
        // Em uma aplicação real: desloga o usuário e redireciona para a página inicial/login
        // Exemplo: this.authService.logoutAndRedirect(); 
      },
      error: (error) => {
        console.error('Erro ao excluir conta:', error);
        this.errorMessage = 'Falha na exclusão. Tente novamente mais tarde.';
        this.isLoading = false;
      }
    });
  }

  // Métodos de navegação (simulados)
  goToProfile(): void {
    this.router.navigate(['/user']);
  }

  goToEdit(): void {
    this.router.navigate(['/edit-user']);
  }

  goToDelete(): void {
    this.router.navigate(['/delete-user']);
  }

  goHome() {
    this.router.navigate(['/home']);
  }
}
