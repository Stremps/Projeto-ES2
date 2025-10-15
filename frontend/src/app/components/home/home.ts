import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

interface Event {
  id: number;
  title: string;
  date: string;
  color: 'blue' | 'orange';
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent {
  isAdmin = false;
  searchTerm: string = '';
  activeTab: string = 'todos'; // 'todos', 'inscritos', 'meus'

  // Placeholder events data
  events: Event[] = [
    { id: 1, title: 'BLUEK OUTUBRO 2025', date: '10/10 - 15/10', color: 'blue' },
    { id: 2, title: 'NOITE RUN - OUTUBRO', date: '10/10 - 15/10', color: 'orange' },
    { id: 3, title: 'BLUEK OUTUBRO 2025', date: '10/10 - 15/10', color: 'blue' },
    { id: 4, title: 'BLUEK OUTUBRO 2025', date: '10/10 - 15/10', color: 'orange' },
    { id: 5, title: 'BLUEK OUTUBRO 2025', date: '10/10 - 15/10', color: 'blue' },
    { id: 6, title: 'BLUEK OUTUBRO 2025', date: '10/10 - 15/10', color: 'orange' },
    { id: 7, title: 'BLUEK OUTUBRO 2025', date: '10/10 - 15/10', color: 'blue' },
    { id: 8, title: 'BLUEK OUTUBRO 2025', date: '10/10 - 15/10', color: 'orange' }
  ];

  filteredEvents(): Event[] {
    return this.events.filter(event =>
      event.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      this.searchTerm === ''
    );
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }
}
