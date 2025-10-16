import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Evento } from '../evento/evento';

interface EventData {
  id: number;
  title: string;
  date: string;
  color: 'blue' | 'orange';
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
  imports: [CommonModule, FormsModule, Evento],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent {
  isAdmin = false;
  searchTerm: string = '';
  activeTab: string = 'todos';
  currentView: 'list' | 'event-details' | 'palestras' = 'list';
  selectedEvent: EventData | null = null;

  events: EventData[] = [
    { 
      id: 1, 
      title: 'BLUEKIDS OUTUBRO 2025', 
      date: '18/12/2025 - 20/12/2025', 
      color: 'blue',
      state: 'PR',
      city: 'Foz do Iguaçu',
      startDate: '30/10/2025',
      endDate: '02/11/2025',
      startTime: '08:00',
      endTime: '17:00',
      bairro: 'Vila Yolanda',
      rua: 'Av. das Cataratas',
      numero: '150',
      cep: '85873-190',
      complemento: 'Em frente ao correio',
      description: 'Comemoração do dia das crianças\n\nVenha participar! Nossas atividades incluem:\n• Brincadeiras e jogos\n• Muita comida\n• Atividades com os pais',
      palestras: [
        {
          id: 1,
          title: 'Alimentação Saudável',
          date: '18/12/2025 - 20/12/2025',
          time: '15:00 - 16:30',
          room: 'Sala 2, Bloco 1',
          speaker: 'João Silva',
          description: 'Venha aprender sobre como alimentar a sua criança de forma saudável e lúdica!',
          slots: 10,
          isExpanded: false
        },
        {
          id: 2,
          title: 'Desenvolvimento Infantil',
          date: '18/12/2025 - 20/12/2025',
          time: '10:00 - 11:30',
          room: 'Sala 1, Bloco 2',
          speaker: 'Maria Santos',
          description: 'Aprenda sobre as fases do desenvolvimento infantil e como estimular seu filho.',
          slots: 15,
          isExpanded: false
        }
      ]
    },
    { id: 2, title: 'NIGHTS RUN - OUTUBRO', date: '10/10 - 15/10', color: 'orange' },
    { id: 3, title: 'BLUEKIDS OUTUBRO 2025', date: '10/10 - 15/10', color: 'blue' }
  ];

  get filteredEvents(): EventData[] {
    return this.events.filter(event =>
      event.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      this.searchTerm === ''
    );
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
    console.log('🔄 Toggle:', palestra.title, '→', palestra.isExpanded);
  }
}
