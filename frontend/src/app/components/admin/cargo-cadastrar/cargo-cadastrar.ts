import { Component, inject, ChangeDetectionStrategy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AdminService, Cargo, CargoRequest } from '../../../services/admin/admin-service';
import { Router } from '@angular/router'; // Import Router for navigation

@Component({
  selector: 'app-cargo-cadastrar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cargo-cadastrar.html',
  styleUrls: ['./cargo-cadastrar.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CargoCadastrar {
  // Injeta os serviços necessários
  private adminService = inject(AdminService);
  private router = inject(Router); // Inject Router

  // Usa signals para gerenciar o estado
  isLoading = signal(false); // Inicia como false, pois não há carregamento inicial
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  // Modelo para o formulário de novo cargo
  novoCargo: CargoRequest = {
    nome: ''
  };

  // ngOnInit não é mais necessário aqui, pois não carregamos cargos existentes

  /**
   * Chamado ao submeter o formulário de novo cargo.
   */
  onSubmit(form: NgForm): void {
    this.errorMessage.set(null); // Limpa erros
    this.successMessage.set(null); // Limpa sucessos

    // Validação básica do formulário
    if (!form.valid || !this.novoCargo.nome?.trim()) {
      this.errorMessage.set('O nome do cargo não pode estar vazio.');
      return;
    }

    // Formata o nome (maiúsculas, sem espaços extras)
    const nomeCargoFormatado = this.novoCargo.nome.toUpperCase().trim();
    if (nomeCargoFormatado.length === 0) {
      this.errorMessage.set('O nome do cargo não pode estar vazio após formatação.');
      return;
    }

    // A verificação de duplicidade agora deve ser feita pelo backend
    // Idealmente, você chamaria um getCargos() aqui antes de salvar,
    // mas vamos confiar na validação do backend por simplicidade.

    const cargoRequest: CargoRequest = { nome: nomeCargoFormatado };
    this.isLoading.set(true); // Mostra feedback visual de carregamento

    // Chama o serviço para criar o cargo
    this.adminService.criarCargo(cargoRequest).subscribe({
      next: (cargoCriado: Cargo) => {
        this.successMessage.set(`Cargo "${cargoCriado.nomeCargo}" criado com sucesso!`);
        form.resetForm(); // Limpa o input do formulário
        this.novoCargo.nome = ''; // Reseta o modelo explicitamente
        this.isLoading.set(false);

        // Limpa a mensagem de sucesso após alguns segundos
        setTimeout(() => this.successMessage.set(null), 3000);
        // Opcional: Redirecionar para outra página ou fechar modal
        // this.router.navigate(['/algum-lugar']);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Erro ao criar cargo:', err);
        // Trata erro de duplicidade especificamente, se o backend retornar status 409 (Conflict) ou similar
        if (err.status === 409 || err.error?.message?.includes('já existe')) {
           this.errorMessage.set(`O cargo "${nomeCargoFormatado}" já existe.`);
        } else {
           this.handleApiError(err, 'criar'); // Usa o handler genérico para outros erros
        }
        this.isLoading.set(false);
      }
    });
  }

  /**
  * Navega de volta para a página home.
  */
  onCancel(): void {
    this.router.navigate(['/home']);
  }


  /**
   * Centraliza o tratamento de erros da API.
   * @param err O objeto HttpErrorResponse.
   * @param acao Ação que estava sendo realizada ('criar').
   */
  private handleApiError(err: HttpErrorResponse, acao: 'criar'): void { // Removido 'carregar' e 'deletar'
    let userMessage = `Falha ao ${acao} cargo.`;

    if (err.status === 403) {
      userMessage = 'Acesso negado. Verifique suas permissões de administrador.';
    } else if (err.error?.message) {
      // Tenta usar a mensagem específica do backend, se disponível
      userMessage = `Erro ao ${acao} cargo: ${err.error.message}`;
    } else if (err.message) {
      // Usa a mensagem genérica do erro HTTP
      userMessage = `Erro ao ${acao} cargo: ${err.message}`;
    } else {
      userMessage = `Erro desconhecido ao ${acao} cargo. Tente novamente.`;
    }

    this.errorMessage.set(userMessage);
  }
}

