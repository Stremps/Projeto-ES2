import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { PalestraRequest } from '../../../interfaces/evento-interface';

@Component({
  selector: 'app-palestra-cadastrar',
  imports: [CommonModule, FormsModule],
  templateUrl: './palestra-cadastrar.html',
  styleUrl: './palestra-cadastrar.css'
})
export class PalestraCadastrar {
  @Input() palestra: PalestraRequest | null = null;
  @Input() eventoId!: number; // O ID do evento é obrigatório
  
  @Output() save = new EventEmitter<PalestraRequest>();
  @Output() cancel = new EventEmitter<void>();

  formData: PalestraRequest = this.createEmptyRequest();
  formTitle: string = 'Criar Palestra';

  ngOnInit(): void {
    if (this.palestra) {
      this.formTitle = 'Editar Palestra';
      this.formData = { ...this.palestra };
    } else {
      this.formTitle = 'Criar Palestra';
      this.formData = this.createEmptyRequest();
    }
  }

  private createEmptyRequest(): PalestraRequest {
    return {
      titulo: '',
      data: '',
      horaInicio: '',
      horaFim: '',
      descricao: '',
      localInterno: '',
      numeroVagas: 0,
      eventoId: this.eventoId,
      palestranteEmail: ''
    };
  }

  onSubmit(form: NgForm): void {
    if (form.valid) {
      this.save.emit(this.formData);
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
