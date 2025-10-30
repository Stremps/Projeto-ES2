export interface EnderecoResponseDto {
  id: number;
  logradouro: string;
  numero: string;
  bairro: string;
  cidade: string;
  uf: string;
  cep: string;
}

export interface PalestranteResponseDto {
  id: string;
  nome: string;
}

export interface PalestraResponseDto {
    id: number;
    titulo: string;
    data: string; // LocalDate
    horaInicio: string; // LocalTime
    horaFim: string; // LocalTime
    localInterno: string;
    numeroVagas: number;
    palestrante: PalestranteResponseDto;
    evento: { id: number; nome: string; };
}

export interface EventoResponseDto {
  id: number;
  nome: string;
  dataInicio: string; // LocalDate
  dataFim: string; // LocalDate
  horaInicio: string; // LocalTime
  horaFim: string; // LocalTime
  descricao: string;
  criador: PalestranteResponseDto;
  endereco: EnderecoResponseDto;
  palestras?: PalestraResponseDto[]; // Adicionado como opcional
}

export interface EventoRequest {
  nome: string;
  dataInicio: string;
  dataFim: string;
  horaInicio: string;
  horaFim: string;
  descricao: string;
  cep: string;
  complemento: string;
  numero: string;
  nomeBairro: string;
  nomeCidade: string;
  siglaUf: string;
  nomeLogradouro: string;
  nomeTipoLogradouro: string;
}

export interface PalestraRequest {
  titulo: string;
  data: string;
  horaInicio: string;
  horaFim: string;
  descricao: string;
  localInterno: string;
  numeroVagas: number;
  eventoId: number;
  palestranteEmail: string;
}

// --- NOVAS INTERFACES ADICIONADAS ---

/**
 * Interface para os tipos de participação (ex: PALESTRANTE, OUVINTE)
 * Conforme PDF 2.1. Listar Tipos de Participação
 */
export interface TipoParticipacao {
  id: number;
  nome: string;
}

/**
 * Interface para o JSON de requisição de inscrição em evento
 * Conforme PDF 2.2. Inscrever-se num Evento
 * ATENÇÃO: Mantendo o typo "tipoParticipacaold" para bater com a API
 */
export interface EventoInscricaoRequest {
  tipoParticipacaoId: number;
}

/**
 * Interface para a resposta da inscrição em evento
 * Conforme PDF 2.2.
 */
export interface VinculoEventoResponseDto {
  id: number;
  nomeParticipante: string;
  nomeEvento: string;
  tipoParticipacao: string;
}