import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { SelectModule } from 'primeng/select';
import { Router, RouterLink } from "@angular/router";
import { HttpClient, HttpClientModule } from '@angular/common/http'; // 1. IMPORTE O HTTPCLIENT

@Component({
  selector: 'app-register',
  standalone: true,
  // 2. ADICIONE O HTTPCLIENTMODULE AOS IMPORTS
  imports: [CommonModule, CardModule, FormsModule, SelectModule, RouterLink, HttpClientModule], 
  templateUrl: './register1.html',
  styleUrl: './register1.css'
})
export class Register1 {
  // ... (todas as suas propriedades existentes permanecem as mesmas) ...
  etapa: number = 1;
  nomeParticipante: string = '';
  emailParticipante: string = '';
  senhaParticipante: string = '';
  telefoneParticipante: string = '';
  cargo: string = '';
  mostrarSenha = false;
  cargos: { label: string; value: string }[] = [
    { label: 'Selecionar', value: '' },
    { label: 'Aluno', value: 'ALUNO' },
    { label: 'Professor', value: 'PROFESSOR' },
    { label: 'Externo', value: 'EXTERNO' }
  ];
  errorNomeParticipante: string | null = null;
  errorEmailParticipante: string | null = null;
  errorSenhaParticipante: string | null = null;
  errorTelefoneParticipante: string | null = null;
  errorCargo: string | null = null;
  cep: string = '';
  nomeTipoLogradouro: string = '';
  nomeLogradouro: string = '';
  numero: string = '';
  complemento: string = '';
  nomeBairro: string = '';
  nomeCidade: string = '';
  siglaUf: string = '';
  errorCep: string | null = null;
  errorNomeTipoLogradouro: string | null = null;
  errorNomeLogradouro: string | null = null;
  errorNumero: string | null = null;
  errorNomeBairro: string | null = null;
  errorNomeCidade: string | null = null;
  errorSiglaUf: string | null = null;
  isSubmitting = false;
  
  // 3. INJETE O HTTPCLIENT E O ROUTER NO CONSTRUTOR
  constructor(private http: HttpClient, private router: Router) {}

  // ... (todos os seus métodos de validação e navegação permanecem os mesmos) ...
  validateNome = (nome: string) => !nome.trim() ? 'Nome é obrigatório.' : null;
  validateEmail = (email: string) => !email.trim() ? 'E-mail é obrigatório.' : null;
  validateSenha = (senha: string) => !senha ? 'Senha é obrigatória.' : null;
  validateTelefone = (tel: string) => !tel.trim() ? 'Telefone é obrigatório.' : null;
  validateCargo = (cargo: string) => !cargo ? 'Selecione um cargo.' : null;
  validateCampoObrigatorio = (campo: string, nome: string) => !campo.trim() ? `${nome} é obrigatório.` : null;
  validateUf = (uf: string) => !uf.trim() ? 'UF é obrigatório.' : null;
  validateCep = (cep: string) => !cep.trim() ? 'CEP é obrigatório.' : null;
  onToggleSenha() {
    this.mostrarSenha = !this.mostrarSenha;
  }

  // Navegação entre etapas
  proximaEtapa() {
    this.errorNomeParticipante = this.validateNome(this.nomeParticipante);
    this.errorEmailParticipante = this.validateEmail(this.emailParticipante);
    this.errorSenhaParticipante = this.validateSenha(this.senhaParticipante);
    this.errorTelefoneParticipante = this.validateTelefone(this.telefoneParticipante);
    this.errorCargo = this.validateCargo(this.cargo);

    if (!this.errorNomeParticipante && !this.errorEmailParticipante && !this.errorSenhaParticipante && !this.errorTelefoneParticipante && !this.errorCargo) {
      this.etapa = 2;
    }
  }

  etapaAnterior() {
    this.etapa = 1;
  }

  // 4. ATUALIZE O MÉTODO ONSUBMIT
  onSubmit() {
    // ... (toda a lógica de validação da etapa 2 permanece a mesma)
    this.errorCep = this.validateCep(this.cep);
    this.errorNomeTipoLogradouro = this.validateCampoObrigatorio(this.nomeTipoLogradouro, 'Tipo de Logradouro');
    this.errorNomeLogradouro = this.validateCampoObrigatorio(this.nomeLogradouro, 'Logradouro');
    this.errorNumero = !this.numero.trim() ? 'Número é obrigatório' : null;
    this.errorNomeBairro = this.validateCampoObrigatorio(this.nomeBairro, 'Bairro');
    this.errorNomeCidade = this.validateCampoObrigatorio(this.nomeCidade, 'Cidade');
    this.errorSiglaUf = this.validateUf(this.siglaUf);

    if (this.errorCep || this.errorNomeTipoLogradouro || this.errorNomeLogradouro || this.errorNumero || this.errorNomeBairro || this.errorNomeCidade || this.errorSiglaUf) {
      return;
    }

    this.isSubmitting = true;
    
    // O objeto de dados já está no formato correto que você precisa
    const dadosCompletos = {
      nomeParticipante: this.nomeParticipante,
      emailParticipante: this.emailParticipante,
      senhaParticipante: this.senhaParticipante,
      telefoneParticipante: this.telefoneParticipante,
      cargo: this.cargo,
      cep: this.cep,
      complemento: this.complemento,
      numero: this.numero,
      nomeBairro: this.nomeBairro,
      nomeCidade: this.nomeCidade,
      siglaUf: this.siglaUf,
      nomeLogradouro: this.nomeLogradouro,
      nomeTipoLogradouro: this.nomeTipoLogradouro
    };

    const apiUrl = 'http://localhost:8081/api/auth/register';

    this.http.post(apiUrl, dadosCompletos, { responseType: 'text' }).subscribe({ // responseType: 'text' pois o retorno é uma string simples
      next: (response) => {
        console.log('Resposta do servidor:', response);
        alert('Registrado com sucesso!');
        this.isSubmitting = false;
        this.router.navigate(['/login']); // Redireciona para a página de login
      },
      error: (err) => {
        console.error('Erro ao cadastrar:', err);
        // Você pode adicionar uma mensagem de erro mais específica aqui
        alert('Erro ao realizar o cadastro. Verifique os dados e tente novamente.');
        this.isSubmitting = false;
      }
    });
  }
}