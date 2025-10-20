import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { EventoRequest } from '../../../interfaces/evento-interface';

@Component({
  selector: 'app-evento-cadastrar',
  imports: [CommonModule, FormsModule],
  templateUrl: './evento-cadastrar.html',
  styleUrl: './evento-cadastrar.css'
})
export class EventoCadastrar {
  // Recebe o evento existente para edição ou um objeto vazio para criação
  @Input() evento: EventoRequest | null = null;
  
  // Emite o evento para ser salvo (criado ou atualizado)
  @Output() save = new EventEmitter<EventoRequest>();
  
  // Emite um evento para cancelar a operação
  @Output() cancel = new EventEmitter<void>();

  // Modelo de dados para o formulário
  formData: EventoRequest = this.createEmptyRequest();

  // Título do formulário (Criar ou Editar)
  formTitle: string = 'Criar Evento';

  ngOnInit(): void {
    if (this.evento) {
      this.formTitle = 'Editar Evento';
      // Clona o objeto para evitar modificar o original diretamente
      this.formData = { ...this.evento };
    } else {
      this.formTitle = 'Criar Evento';
      this.formData = this.createEmptyRequest();
    }
  }

  // Função para criar um objeto de requisição vazio
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
  
  // Manipulador de submissão do formulário
  onSubmit(form: NgForm): void {
    if (form.valid) {
      this.save.emit(this.formData);
    }
  }

  // Manipulador para o botão cancelar
  onCancel(): void {
    this.cancel.emit();
  }
}
