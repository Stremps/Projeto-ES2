import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interface para o Cargo (baseado no backend)
// O backend retorna um array de strings (nomes dos enums) para GET /api/cargos
// Mas para POST e DELETE, ele trabalha com a entidade Cargo que tem id e nomeCargo
export interface Cargo {
  id: number; // Assumindo que o POST retorna um objeto com ID
  nomeCargo: string;
}

// Interface para criar um novo Cargo (via POST /api/cargos)
export interface CargoRequest {
  nome: string;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);
  // URL base da API (ajuste se necessário)
  private apiUrl = 'http://localhost:8081/api'; // Usando porta 8081 como nos seus outros serviços

  constructor() { }

  /**
   * Busca todos os cargos do backend.
   * Ajustado para esperar um array de strings como no seu CargoController.java
   * e mapear para a interface Cargo com um ID temporário (ou 0).
   * Se o backend for atualizado para retornar {id, nomeCargo}, remova o .pipe(map(...))
   */
  getCargos(): Observable<Cargo[]> {
    // A rota GET /api/cargos no seu backend retorna um array de strings (nomes dos enums)
    // Se você quer exibir IDs, o backend precisa retornar objetos {id, nomeCargo}
    // Por enquanto, vamos retornar os nomes como estão e usar um ID genérico
    return this.http.get<Cargo[]>(`${this.apiUrl}/tipos-participacao`);

     /* Exemplo se o backend retornasse array de strings:
     return this.http.get<string[]>(`${this.apiUrl}/cargos`).pipe(
       map((nomes) => nomes.map((nome, index) => ({ id: index + 1, nomeCargo: nome })))
     );
     */
  }

  /**
   * Cria um novo cargo no backend.
   * (Corresponde a POST /api/cargos - ATENÇÃO: Seu backend não tem essa rota implementada!)
   * Você precisará adicionar essa funcionalidade no CargoController.java
   */
  criarCargo(cargo: CargoRequest): Observable<Cargo> {
    return this.http.post<Cargo>(`${this.apiUrl}/tipos-participacao`, cargo);
  }
}
