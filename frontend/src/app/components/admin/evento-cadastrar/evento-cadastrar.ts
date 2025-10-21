import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { EventoRequest } from '../../../interfaces/evento-interface';
import { EventoService } from '../../../services/evento/evento-service';

@Component({
  selector: 'app-evento-cadastrar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './evento-cadastrar.html',
  styleUrls: ['./evento-cadastrar.css']
})
export class EventoCadastrar implements OnInit {
  private eventoService = inject(EventoService);
  private router = inject(Router);

  // A lógica de edição seria em outra rota, por exemplo, 'editar-evento/:id'.
  formData: EventoRequest = this.createEmptyRequest();
  formTitle: string = 'Criar Evento';
  isSubmitting = false;
  errorMessage: string | null = null;

  ngOnInit(): void {
    // Como esta é a tela de cadastro, apenas inicializamos um formulário vazio.
    this.formTitle = 'Criar Evento';
    this.formData = this.createEmptyRequest();
  }

  private createEmptyRequest(): EventoRequest {
    return {
      nome: '',
      dataInicio: '',
      dataFim: '',
      horaInicio: '',
      horaFim: '',
      descricao: '',
      cep: '',
      complemento: '',
      numero: '',
      nomeBairro: '',
      nomeCidade: '',
      siglaUf: '',
      nomeLogradouro: '',
      nomeTipoLogradouro: 'Rua' // Valor padrão
    };
  }

  onSubmit(form: NgForm): void {
    if (!form.valid || this.isSubmitting) {
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;

    this.eventoService.criarEvento(this.formData).subscribe({
      next: (response) => {
        console.log('Evento criado com sucesso:', response);
        alert('Evento criado com sucesso!');
        this.isSubmitting = false;
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Erro ao criar evento:', err);
        this.errorMessage = `Ocorreu um erro ao criar o evento: ${err.error?.message || 'Verifique os dados e tente novamente.'}`;
        this.isSubmitting = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/home']);
  }
}
