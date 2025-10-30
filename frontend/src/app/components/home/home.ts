import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Evento } from '../evento/evento';
import { EventoService } from '../../services/evento/evento-service';
import { 
  EventoResponseDto, 
  PalestraResponseDto,
  TipoParticipacao,  // IMPORTADO
  VinculoEventoResponseDto // IMPORTADO
} from '../../interfaces/evento-interface';
import { Router, RouterLink } from "@angular/router";
import { Auth } from '../../services/auth';
import { Subscription } from 'rxjs';

// Interface interna do componente para a visualização
export interface EventData {
  id: number;
  title: string;
  date: string;
  color: 'blue' | 'orange';
  // Detalhes do evento
  state?: string;
  city?: string;
  startDate?: string;
  endDate?: string;
  startTime?: string;
  endTime?: string;
  bairro?: string;
  rua?: string;
  numero?: string;
  cep?: string;
  complemento?: string;
  description?: string;
  palestras?: Palestra[];
}

// Interface interna para palestras
interface Palestra {
  id: number;
  title: string;
  date: string;
  time: string;
  room: string;
  speaker?: string;
  description?: string;
  slots?: number;
  isExpanded: boolean;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, Evento, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent implements OnInit, OnDestroy {
  isAdmin = false;
  searchTerm: string = '';
  activeTab: string = 'todos';
  currentView: 'list' | 'event-details' | 'palestras' = 'list';
  selectedEvent: EventData | null = null;

  // Gerenciamento de estado da UI
  isLoading = true;
  errorMessage: string | null = null;

  // --- NOVOS ESTADOS PARA O MODAL DE INSCRIÇÃO ---
  showInscricaoModal = false;
  tiposParticipacao: TipoParticipacao[] = [];
  selectedTipoParticipacaoId: number | null = null;
  inscricaoErrorMessage: string | null = null;
  isSubmittingInscricao = false;
  // --- FIM DOS NOVOS ESTADOS ---

  // Serviços injetados
  private authService = inject(Auth);
  private eventoService = inject(EventoService);
  private router = inject(Router);

  // Subscription para o status de admin do usuário
  private adminStatusSubscription: Subscription | undefined;

  // Lista de eventos que será preenchida pela API
  allEvents: EventData[] = [];
  filteredEvents: EventData[] = [];

  ngOnInit(): void {
    // ATUALIZADO: Verifica se o usuário é admin de forma reativa
    this.adminStatusSubscription = this.authService.isAdmin$.subscribe(isAdmin => {
      this.isAdmin = isAdmin;
    });
    this.loadEvents();
  }

  ngOnDestroy(): void {
    // Limpa a subscription para evitar memory leaks
    this.adminStatusSubscription?.unsubscribe();
  }

  // Método para carregar os eventos da API
  loadEvents(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.eventoService.getEventos().subscribe({
      next: (eventosDto) => {
        this.allEvents = eventosDto.map((dto, index) => this.mapDtoToEventData(dto, index));
        this.filterEvents(); 
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar eventos:', err);
        this.errorMessage = 'Não foi possível carregar os eventos. Tente novamente mais tarde.';
        this.isLoading = false;
      }
    });
  }

  // Lógica de filtro para usar o novo array
  filterEvents(): void {
    const searchTermLower = this.searchTerm.toLowerCase();
    this.filteredEvents = this.allEvents.filter(event =>
      event.title.toLowerCase().includes(searchTermLower)
    );
  }

  onDeleteEvent(): void {
    if (!this.selectedEvent) return;

    // Substituindo confirm() por uma solução que não bloqueia
    const confirmed = window.confirm(`Tem certeza que deseja deletar o evento "${this.selectedEvent.title}"?`);
    if (!confirmed) return;

    this.eventoService.excluirEvento(this.selectedEvent.id).subscribe({
      next: () => {
        alert('Evento deletado com sucesso!');
        this.selectedEvent = null;
        this.loadEvents(); // recarrega a lista de eventos atualizada
        this.currentView = 'list'; // volta para a lista de eventos
      },
      error: (err) => {
        alert('Erro ao deletar o evento. Tente novamente mais tarde.');
        console.error(err);
      }
    });
  }


  // Método auxiliar para mapear os dados
  private mapDtoToEventData(dto: EventoResponseDto, index: number): EventData {
    return {
      id: dto.id,
      title: dto.nome,
      date: `${this.formatDate(dto.dataInicio)} - ${this.formatDate(dto.dataFim)}`,
      color: index % 2 === 0 ? 'blue' : 'orange', // Alterna as cores
      state: dto.endereco.uf,
      city: dto.endereco.cidade,
      startDate: this.formatDate(dto.dataInicio),
      endDate: this.formatDate(dto.dataFim),
      startTime: dto.horaInicio,
      endTime: dto.horaFim,
      bairro: dto.endereco.bairro,
      rua: dto.endereco.logradouro,
      numero: dto.endereco.numero,
      cep: dto.endereco.cep,
      complemento: dto.endereco.bairro, // Ajuste se tiver complemento no DTO
      description: dto.descricao,
      palestras: dto.palestras ? dto.palestras.map(this.mapPalestraDtoToPalestra) : []
    };
  }

  private mapPalestraDtoToPalestra(palestraDto: PalestraResponseDto): Palestra {
      return {
          id: palestraDto.id,
          title: palestraDto.titulo,
          date: new Date(palestraDto.data).toLocaleDateString(),
          time: `${palestraDto.horaInicio} - ${palestraDto.horaFim}`,
          room: palestraDto.localInterno,
          speaker: palestraDto.palestrante.nome,
          description: "Descrição da palestra aqui...", // Adicionar se vier da API
          slots: palestraDto.numeroVagas,
          isExpanded: false
      };
  }

  // Função simples para formatar a data
  private formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const [year, month, day] = dateStr.split('-');
    return `${day}/${month}/${year}`;
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  openEventDetails(event: EventData): void {
    this.selectedEvent = event;
    this.currentView = 'event-details';
  }

  openPalestras(): void {
    this.currentView = 'palestras';
  }

  backToList(): void {
    this.currentView = 'list';
    this.selectedEvent = null;
  }

  backToEventDetails(): void {
    this.currentView = 'event-details';
  }

  togglePalestra(palestra: Palestra): void {
    palestra.isExpanded = !palestra.isExpanded;
  }

  logout(): void {
    this.authService.logout();
  }

  // --- NOVOS MÉTODOS PARA O MODAL DE INSCRIÇÃO ---

  openInscricaoModal(): void {
    this.showInscricaoModal = true;
    this.inscricaoErrorMessage = null;
    this.selectedTipoParticipacaoId = null;
    this.isSubmittingInscricao = false;
    
    // Busca os tipos de participação da API
    this.eventoService.getTiposParticipacao().subscribe({
      next: (tipos) => {
        this.tiposParticipacao = tipos;
      },
      error: (err) => {
        console.error('Erro ao buscar tipos de participação:', err);
        this.inscricaoErrorMessage = 'Erro ao carregar os tipos de participação. Tente fechar e abrir novamente.';
      }
    });
  }

  closeInscricaoModal(): void {
    if (this.isSubmittingInscricao) return; // Não deixa fechar se estiver enviando
    this.showInscricaoModal = false;
    this.tiposParticipacao = [];
    this.selectedTipoParticipacaoId = null;
    this.inscricaoErrorMessage = null;
  }

  onConfirmInscricao(): void {
    if (!this.selectedEvent || !this.selectedTipoParticipacaoId) {
      this.inscricaoErrorMessage = "Por favor, selecione um tipo de participação.";
      return;
    }

    this.isSubmittingInscricao = true;
    this.inscricaoErrorMessage = null;

    this.eventoService.inscreverEmEvento(this.selectedEvent.id, this.selectedTipoParticipacaoId).subscribe({
      next: (response) => {
        this.isSubmittingInscricao = false;
        alert(`Inscrição como ${response.tipoParticipacao} realizada com sucesso!`);
        this.closeInscricaoModal();
        // AQUI: Você pode querer atualizar a aba 'Eventos Inscritos' ou o estado do evento
      },
      error: (err) => {
        this.isSubmittingInscricao = false;
        console.error('Erro ao inscrever-se no evento:', err);
        // Tenta pegar a mensagem de erro específica do backend
        this.inscricaoErrorMessage = err.error?.message || 'Erro ao realizar inscrição. Você já pode estar inscrito ou o evento está lotado.';
      }
    });
  }

}
