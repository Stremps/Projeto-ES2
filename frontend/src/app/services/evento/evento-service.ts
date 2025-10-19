import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventoResponseDto } from '../../interfaces/evento-interface';

@Injectable({
  providedIn: 'root'
})
export class EventoService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8081/api/eventos';

  /**
   * Busca a lista de todos os eventos do backend.
   * O token de autenticação é adicionado automaticamente pelo AuthInterceptor.
   */
  getEventos(): Observable<EventoResponseDto[]> {
    return this.http.get<EventoResponseDto[]>(this.API_URL);
  }

  // Futuramente, adicionar outros métodos aqui:
  // getEventoById(id: number): Observable<EventoResponseDto> { ... }
  // criarEvento(evento: EventoRequest): Observable<EventoResponseDto> { ... }
}
