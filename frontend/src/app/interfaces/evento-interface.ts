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