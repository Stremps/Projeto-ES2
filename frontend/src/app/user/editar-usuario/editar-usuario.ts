import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Necessário para ngModel (formulários)
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

// Interface para estruturar os dados do usuário, agora editáveis
interface EditableUserProfile {
  nomeCompleto: string;
  estado: string;
  cidade: string;
  telefone: string;
  bairro: string;
  rua: string;
  cargo: string;
  numero: string;
  cep: string;
  email: string;
  complemento: string;
  senha: string; 
}

@Component({
  selector: 'app-editar-usuario',
  standalone: true,
  // Adicionamos FormsModule e HttpClientModule
  imports: [CommonModule, FormsModule, HttpClientModule], 
  templateUrl: './editar-usuario.html',
  styleUrl: './editar-usuario.css'
})
export class EditarUsuario implements OnInit {
  constructor(private router: Router, private http: HttpClient) {}
  
  // Objeto que será editado, inicializado com dados mockados
  user: EditableUserProfile = this.getMockUser();
  isLoading: boolean = true;
  errorMessage: string | null = null;

  // Endpoints de exemplo
  private readonly getProfileApiUrl = 'http://localhost:8081/api/user/profile'; // GET para carregar
  private readonly updateProfileApiUrl = 'http://localhost:8081/api/user/update'; // PUT para salvar

  ngOnInit(): void {
    // Carrega os dados do usuário ao iniciar o componente
    this.loadUserProfile();
  }

  // --- Lógica de Carregamento (GET) ---

  loadUserProfile(): void {
    this.isLoading = true;
    this.errorMessage = null;

    // Simulação da requisição GET para carregar os dados
    this.http.get<any>(this.getProfileApiUrl).subscribe({
      next: (data) => {
        // Mapeia os dados reais para o formulário
        this.user = {
          nomeCompleto: data.nomeCompleto || '',
          estado: data.estado || '',
          cidade: data.cidade || '',
          telefone: data.telefone || '',
          bairro: data.bairro || '',
          rua: data.rua || '',
          cargo: data.cargo || '',
          numero: data.numero || '',
          cep: data.cep || '',
          email: data.email || '',
          complemento: data.complemento || '',
          // A senha é geralmente tratada separadamente, mas aqui mantemos o campo
          senha: '********' 
        };
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar perfil para edição:', error);
        this.errorMessage = 'Não foi possível carregar os dados. Verifique o endpoint.';
        this.isLoading = false;
        // Em caso de erro, mantém os dados mockados para permitir a edição
      }
    });
  }

  // --- Lógica de Salvamento (PUT) ---

  saveProfile(): void {
    this.isLoading = true;
    this.errorMessage = null;

    // Prepara o objeto de dados para envio (você pode precisar de um DTO diferente aqui)
    const dataToSave = { ...this.user }; 
    
    // Requisição HTTP PUT para salvar as alterações
    this.http.put(this.updateProfileApiUrl, dataToSave).subscribe({
      next: (response) => {
        console.log('Perfil atualizado com sucesso:', response);
        this.isLoading = false;
        // Redireciona de volta para a tela de visualização ou exibe sucesso
        this.goToProfile(); 
      },
      error: (error) => {
        console.error('Erro ao salvar o perfil:', error);
        this.errorMessage = 'Erro ao salvar as alterações. Tente novamente.';
        this.isLoading = false;
      }
    });
  }

  // --- Dados Mockados e Navegação ---

  private getMockUser(): EditableUserProfile {
    // Dados iniciais para o formulário
    return {
      nomeCompleto: 'João Silva',
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
      senha: '********' // Valor inicial para o campo de senha
    };
  }

  // Métodos de navegação (simulados)

  goToProfile(): void {
    this.router.navigate(['/user']);
  }

  goToEdit(): void {
    this.router.navigate(['/edit-user']);
  }

  goToDelete(): void {
    this.router.navigate(['/delete-user']);
  }

  cancelEdit(): void {
    console.log('Cancelar edição. Voltando para Perfil.');
    this.goToProfile();
  }

}
