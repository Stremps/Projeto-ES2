import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Evento } from '../evento/evento';
import { EventoService } from '../../services/evento/evento-service';
import { EventoResponseDto, PalestraResponseDto } from '../../interfaces/evento-interface';
import { Router, RouterLink } from "@angular/router";
import { Auth } from '../../services/auth';

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
export class HomeComponent implements OnInit {
  isAdmin = false;
  searchTerm: string = '';
  activeTab: string = 'todos';
  currentView: 'list' | 'event-details' | 'palestras' = 'list';
  selectedEvent: EventData | null = null;

  // Gerenciamento de estado da UI
  isLoading = true;
  errorMessage: string | null = null;

  // Serviços injetados
  private authService = inject(Auth);
  private eventoService = inject(EventoService); // 5. Injetar o serviço de eventos
  private router = inject(Router);

  // Lista de eventos que será preenchida pela API
  allEvents: EventData[] = [];
  filteredEvents: EventData[] = [];

  ngOnInit(): void {
    this.loadEvents();
  }

  // 6. Método para carregar os eventos da API
  loadEvents(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.eventoService.getEventos().subscribe({
      next: (eventosDto) => {
        // 7. Mapear o DTO do backend para o formato do frontend
        this.allEvents = eventosDto.map((dto, index) => this.mapDtoToEventData(dto, index));
        this.filterEvents(); // Filtra os eventos após carregar
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar eventos:', err);
        this.errorMessage = 'Não foi possível carregar os eventos. Tente novamente mais tarde.';
        this.isLoading = false;
      }
    });
  }

  // Atualizar a lógica de filtro para usar o novo array
  filterEvents(): void {
    const searchTermLower = this.searchTerm.toLowerCase();
    this.filteredEvents = this.allEvents.filter(event =>
      event.title.toLowerCase().includes(searchTermLower)
    );
  }

  // 8. Método auxiliar para mapear os dados
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
}
