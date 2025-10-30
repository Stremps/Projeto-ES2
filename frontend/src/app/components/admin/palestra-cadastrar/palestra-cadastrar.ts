import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PalestraRequest } from '../../../interfaces/evento-interface';
// Importa o serviço para interagir com o backend
import { EventoService } from '../../../services/evento/evento-service';

@Component({
  selector: 'app-palestra-cadastrar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './palestra-cadastrar.html',
  styleUrls: ['./palestra-cadastrar.css']
})
export class PalestraCadastrar implements OnInit {
  // Injeta os serviços necessários
  private eventoService = inject(EventoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  eventoId!: number; // ID do evento ao qual a palestra pertence
  formData: PalestraRequest = this.createEmptyRequest(); // Dados do formulário
  formTitle: string = 'Criar Palestra'; // Título da página
  isSubmitting = false; // Flag para indicar se o formulário está sendo enviado
  errorMessage: string | null = null; // Mensagem de erro

  ngOnInit(): void {
    // Pega o ID do evento da rota na inicialização do componente
    const id = this.route.snapshot.paramMap.get('eventoId');
    if (id) {
      // Converte o ID para número e o armazena
      this.eventoId = +id;
      // Inicializa o formulário com o ID do evento
      this.formData = this.createEmptyRequest();
    } else {
      // Se não encontrar o ID, loga um erro e redireciona para a home
      console.error('ID do evento não fornecido na rota.');
      this.router.navigate(['/home']);
    }
  }

  // Cria um objeto PalestraRequest vazio com o ID do evento atual
  private createEmptyRequest(): PalestraRequest {
    return {
      titulo: '',
      data: '',
      horaInicio: '',
      horaFim: '',
      descricao: '',
      localInterno: '',
      numeroVagas: 0, // Inicializa com 0 ou outro valor padrão
      eventoId: this.eventoId, // Associa o ID do evento
      palestranteEmail: ''
    };
  }

  // Função chamada ao submeter o formulário
  onSubmit(form: NgForm): void {
    // Verifica se o formulário é válido ou se já está enviando
    if (!form.valid || this.isSubmitting) {
      this.errorMessage = 'Formulário inválido. Verifique os campos.';
      return;
    }

    this.isSubmitting = true; // Define o estado como enviando
    this.errorMessage = null; // Limpa mensagens de erro anteriores

    // *** INTEGRAÇÃO COM BACKEND ***
    // Chama o método criarPalestra do serviço, passando os dados do formulário
    this.eventoService.criarPalestra(this.formData).subscribe({
      next: (response) => {
        // Callback de sucesso
        console.log('Palestra criada com sucesso:', response);
        // Exibe um alerta de sucesso (idealmente, usar um componente de notificação)
        alert('Palestra criada com sucesso!');
        this.isSubmitting = false; // Define o estado como não enviando
        // Navega de volta para a home após o sucesso
        this.router.navigate(['/home']);
      },
      error: (err) => {
        // Callback de erro
        console.error('Erro ao criar palestra:', err);
        // Define a mensagem de erro para ser exibida no template
        this.errorMessage = `Ocorreu um erro: ${err.error?.message || 'Verifique os dados e tente novamente.'}`;
        this.isSubmitting = false; // Define o estado como não enviando
      }
    });
  }

  // Função para o botão Cancelar
  onCancel(): void {
    // Navega de volta para a página home
    this.router.navigate(['/home']);
  }
}
