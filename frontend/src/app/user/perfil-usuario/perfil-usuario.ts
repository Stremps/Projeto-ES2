import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http'; // 1. Importa o HttpClient e o módulo
import { Router } from '@angular/router';

// 2. Adicione o HttpClientModule aos imports do Componente Standalone
@Component({
  selector: 'app-perfil-usuario',
  standalone: true,
  imports: [CommonModule, HttpClientModule], // HttpClientModule adicionado aqui
  templateUrl: './perfil-usuario.html',
  styleUrl: './perfil-usuario.css'
})
export class PerfilUsuario implements OnInit {
  constructor(private router: Router, private http: HttpClient) {}

  // Interface para estruturar os dados do usuário (ajustada para ser opcional inicialmente)
  // Nota: O campo 'senha' é mantido para exibição de '********', mas não deve vir do backend
  // MUDANÇA: Inicializa 'user' com os dados mockados para ter conteúdo na primeira renderização.
  user: any = this.getMockUser();
  isLoading: boolean = true;
  errorMessage: string | null = null;
  
  // URL de exemplo para o endpoint de busca de perfil
  private readonly apiUrl = 'http://localhost:8081/api/user/profile'; // Mude para seu endpoint real

  ngOnInit(): void {
    this.loadUserProfile();
  }

  /**
   * Método responsável por fazer a requisição HTTP GET para buscar os dados do usuário.
   * Assumindo que a autenticação (token) é tratada por um interceptor ou serviço.
   */
  loadUserProfile(): void {
    this.isLoading = true;
    this.errorMessage = null;

    // Requisição HTTP GET
    this.http.get<any>(this.apiUrl).subscribe({
      next: (data) => {
        // Mapeia os dados recebidos do backend para a propriedade 'user'
        this.user = {
          nomeCompleto: data.nomeCompleto || 'N/A',
          estado: data.estado || 'N/A',
          cidade: data.cidade || 'N/A',
          telefone: data.telefone || 'N/A',
          bairro: data.bairro || 'N/A',
          rua: data.rua || 'N/A',
          cargo: data.cargo || 'N/A',
          numero: data.numero || 'N/A',
          cep: data.cep || 'N/A',
          email: data.email || 'N/A',
          complemento: data.complemento || '',
          // Simula a senha para exibição, pois o backend NUNCA deve retornar a senha real
          senha: '********' 
        };
        this.isLoading = false;
        console.log('Dados do usuário carregados com sucesso:', this.user);
      },
      error: (error) => {
        console.error('Erro ao buscar perfil do usuário:', error);
        this.errorMessage = 'Não foi possível carregar os dados do perfil. Tente novamente mais tarde.';
        this.isLoading = false;
        // Se houver erro, mantemos os dados mockados que já estavam na tela
        // (Não há necessidade de re-atribuir, a menos que o mock fosse mais complexo)
        // A linha abaixo é redundante mas mantida para clareza sobre o fluxo em caso de erro:
        // this.user = this.getMockUser(); 
      }
    });
  }
  
  // Função para retornar dados mockados (TEMPORÁRIOS)
  private getMockUser(): any {
    return {
      nomeCompleto: 'João Silva', // Removido o "(MOCK)" para exibir os dados limpos
      estado: 'PR',
      cidade: 'Foz do Iguaçu',
      telefone: '(45) 99932-2300',
      bairro: 'Vila Yolanda',
      rua: 'Av. das Cataratas',
      cargo: 'Estudante',
      numero: '150',
      cep: '85873-190',
      email: 'JoaoSilva@email.com',
      complemento: 'Em frente ao correio',
      senha: '********'
    };
  }


  // Métodos de navegação mockados para o menu lateral
  goToProfile(): void {
    this.router.navigate(['/user']);
  }

  goToEdit(): void {
    this.router.navigate(['/edit-user']);
  }

  goToDelete(): void {
    this.router.navigate(['/delete-user']);
  }

  goHome() {
    this.router.navigate(['/home']);
  }
}
