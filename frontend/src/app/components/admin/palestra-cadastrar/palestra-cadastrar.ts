import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PalestraRequest } from '../../../interfaces/evento-interface';
import { EventoService } from '../../../services/evento/evento-service';

@Component({
  selector: 'app-palestra-cadastrar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './palestra-cadastrar.html',
  styleUrls: ['./palestra-cadastrar.css']
})
export class PalestraCadastrar implements OnInit {
  private eventoService = inject(EventoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  eventoId!: number;
  formData: PalestraRequest = this.createEmptyRequest();
  formTitle: string = 'Criar Palestra';
  isSubmitting = false;
  errorMessage: string | null = null;

  ngOnInit(): void {
    // Pega o ID do evento da URL
    const id = this.route.snapshot.paramMap.get('eventoId');
    if (id) {
      this.eventoId = +id;
      this.formData = this.createEmptyRequest();
    } else {
      // Se não houver ID, redireciona de volta para a home
      console.error('ID do evento não fornecido na rota.');
      this.router.navigate(['/home']);
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
    if (!form.valid || this.isSubmitting) {
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;

    this.eventoService.criarPalestra(this.formData).subscribe({
      next: (response) => {
        console.log('Palestra criada com sucesso:', response);
        alert('Palestra criada com sucesso!');
        this.isSubmitting = false;
        // Volta para a home (ou para a tela de detalhes do evento)
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Erro ao criar palestra:', err);
        this.errorMessage = `Ocorreu um erro: ${err.error?.message || 'Verifique os dados e tente novamente.'}`;
        this.isSubmitting = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/home']);
  }
}
