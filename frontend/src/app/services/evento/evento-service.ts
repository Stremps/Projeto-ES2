import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventoResponseDto, PalestraResponseDto, EventoRequest, PalestraRequest } from '../../interfaces/evento-interface';

@Injectable({
  providedIn: 'root'
})
export class EventoService {
  private http = inject(HttpClient);
  private readonly API_URL_EVENTOS = 'http://localhost:8081/api/eventos';
  private readonly API_URL_PALESTRAS = 'http://localhost:8081/api/palestras';

  // --- MÉTODOS DE EVENTO ---

  getEventos(): Observable<EventoResponseDto[]> {
    return this.http.get<EventoResponseDto[]>(this.API_URL_EVENTOS);
  }
  
  getEventoById(id: number): Observable<EventoResponseDto> {
    return this.http.get<EventoResponseDto>(`${this.API_URL_EVENTOS}/${id}`);
  }

  criarEvento(evento: EventoRequest): Observable<EventoResponseDto> {
    return this.http.post<EventoResponseDto>(this.API_URL_EVENTOS, evento);
  }

  atualizarEvento(id: number, evento: EventoRequest): Observable<EventoResponseDto> {
    return this.http.put<EventoResponseDto>(`${this.API_URL_EVENTOS}/${id}`, evento);
  }

  excluirEvento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL_EVENTOS}/${id}`);
  }

  // --- MÉTODOS DE PALESTRA ---

  criarPalestra(palestra: PalestraRequest): Observable<PalestraResponseDto> {
    return this.http.post<PalestraResponseDto>(this.API_URL_PALESTRAS, palestra);
  }

  atualizarPalestra(id: number, palestra: PalestraRequest): Observable<PalestraResponseDto> {
    return this.http.put<PalestraResponseDto>(`${this.API_URL_PALESTRAS}/${id}`, palestra);
  }

  excluirPalestra(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL_PALESTRAS}/${id}`);
  }
}
