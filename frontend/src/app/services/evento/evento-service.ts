import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  EventoResponseDto, 
  PalestraResponseDto, 
  EventoRequest, 
  PalestraRequest,
  TipoParticipacao,  // IMPORTADO
  EventoInscricaoRequest, // IMPORTADO
  VinculoEventoResponseDto // IMPORTADO
} from '../../interfaces/evento-interface';

@Injectable({
  providedIn: 'root'
})
export class EventoService {
  private http = inject(HttpClient);
  
  // URLs DA API
  private readonly API_URL = 'http://localhost:8081/api';
  private readonly API_URL_EVENTOS = `${this.API_URL}/eventos`;
  private readonly API_URL_PALESTRAS = `${this.API_URL}/palestras`;
  // NOVAS URLs
  private readonly API_URL_TIPOS_PARTICIPACAO = `${this.API_URL}/tipos-participacao`;
  private readonly API_URL_VINCULOS_EVENTO = `${this.API_URL}/vinculos-evento`;


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

  // --- NOVOS MÉTODOS PARA INSCRIÇÃO ---

  /**
   * Busca os tipos de participação disponíveis (ex: PALESTRANTE, OUVINTE)
   * PDF 2.1. Listar Tipos de Participação
   */
  getTiposParticipacao(): Observable<TipoParticipacao[]> {
    return this.http.get<TipoParticipacao[]>(this.API_URL_TIPOS_PARTICIPACAO);
  }

  /**
   * Inscreve o usuário logado em um evento com um tipo de participação.
   * PDF 2.2. Inscrever-se num Evento
   * @param eventoId O ID do evento no qual se inscrever.
   * @param tipoId O ID do Tipo de Participação (ex: 1 para PALESTRANTE, 2 para OUVINTE).
   */
  inscreverEmEvento(eventoId: number, tipoId: number): Observable<VinculoEventoResponseDto> {
    // A API espera o ID do tipo no corpo da requisição
    // ATENÇÃO: O PDF indica um typo no nome do campo: "tipoParticipacaold"
    const body: EventoInscricaoRequest = {
      tipoParticipacaoId: tipoId 
    };
    
    return this.http.post<VinculoEventoResponseDto>(
      `${this.API_URL_VINCULOS_EVENTO}/${eventoId}/inscrever-se`, 
      body
    );
  }
}
