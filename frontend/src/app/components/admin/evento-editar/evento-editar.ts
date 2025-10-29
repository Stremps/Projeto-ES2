import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { EventoRequest, EventoResponseDto } from '../../../interfaces/evento-interface'; 
import { EventoService } from '../../../services/evento/evento-service';

@Component({
  selector: 'app-evento-editar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './evento-editar.html',
  styleUrls: ['./evento-editar.css'],
})
export class EventoEditar implements OnInit {
  private eventoService = inject(EventoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  formData: EventoRequest = this.createEmptyRequest();
  formTitle: string = 'Editar Evento';
  isSubmitting: boolean = false;
  errorMessage: string | null = null;
  
  private eventoId: number | null = null; 

  ngOnInit(): void {
    this.formTitle = 'Editar Evento';
    this.route.paramMap.subscribe((params) => {
      const idParam = params.get('id');
      const id = idParam ? parseInt(idParam, 10) : null; 
      
      if (id && !isNaN(id) && id > 0) {
        this.eventoId = id; 
        this.eventoService.getEventoById(id).subscribe({
          next: (evento) => {
            // Mapeamento para corrigir o erro TS2740 (estrutura de dados)
            this.formData = this.mapResponseToRequest(evento); 
          }
          // error: (err) => {
          //   this.errorMessage =
          //     'Erro ao carregar evento. Tente novamente.\n' + (err.error?.message || '');
          // },
        });
      } else {
        this.errorMessage = 'ID de evento inválido na rota.';
      }
    });
  }
  
  // Método de Mapeamento (DTO de Resposta -> EventoRequest)
  private mapResponseToRequest(evento: any): EventoRequest {
      const response = evento as EventoResponseDto;
      
      return {
          nome: response.nome || '',
          dataInicio: response.dataInicio || '',
          dataFim: response.dataFim || '',
          horaInicio: response.horaInicio || '',
          horaFim: response.horaFim || '',
          descricao: response.descricao || '',
          
          // CORREÇÃO TS2339: O campo 'complemento' não existe em 'EnderecoResponseDto'.
          // É inicializado como vazio, mas é necessário no EventoRequest.
          complemento: '', 

          // Mapeamento dos campos de endereço, usando os nomes de campo do JSON de Retorno.
          cep: response.endereco?.cep || '',
          numero: response.endereco?.numero || '',
          // Atenção à diferença de nome: 'bairro' no Retorno, 'nomeBairro' na Requisição
          nomeBairro: response.endereco?.bairro || '', 
          nomeCidade: response.endereco?.cidade || '',
          siglaUf: response.endereco?.uf || '', // 'uf' no Retorno
          nomeLogradouro: response.endereco?.logradouro || '',
          
          // O campo 'nomeTipoLogradouro' não é retornado, mas é exigido pelo EventoRequest.
          nomeTipoLogradouro: 'Avenida' // Valor padrão baseado no JSON de entrada
      };
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
      nomeTipoLogradouro: 'Rua'
    };
  }

  onSubmit(form: NgForm): void {
    if (!form.valid || !this.eventoId) { 
      this.isSubmitting = false;
      this.errorMessage = this.eventoId ? 'Formulário inválido.' : 'ID do evento não encontrado.';
      return;
    }
    this.isSubmitting = true;
    this.errorMessage = null;
    
    // Método 'atualizarEvento' está sendo usado (Corrigido o TS2339 anterior)
    this.eventoService.atualizarEvento(this.eventoId, this.formData).subscribe({ 
      next: (response) => {
        alert('Evento atualizado com sucesso!');
        this.isSubmitting = false;
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.errorMessage =
          'Ocorreu um erro ao atualizar o evento. ' + (err.error?.message || '');
        this.isSubmitting = false;
      },
    });
  }

  onCancel(): void {
    this.router.navigate(['/home']);
  }
}