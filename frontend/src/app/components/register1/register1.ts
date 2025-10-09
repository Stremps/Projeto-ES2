import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { SelectModule } from 'primeng/select';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, CardModule, FormsModule, SelectModule, RouterLink],
  templateUrl: './register1.html', // Nome do seu arquivo HTML
  styleUrl: './register1.css'     // Nome do seu arquivo CSS
})
export class Register1 {
  // Variável para controlar a etapa atual do formulário
  etapa: number = 1;

  // --- Propriedades da Etapa 1 ---
  nome: string = '';
  email: string = '';
  senha: string = '';
  telefone: string = '';
  cargo: string = '';
  mostrarSenha = false;

  cargos: { label: string; value: string }[] = [
    { label: 'Selecionar', value: '' },
    { label: 'Aluno', value: 'ALUNO' },
    { label: 'Professor', value: 'PROFESSOR' },
    { label: 'Externo', value: 'EXTERNO' }
  ];

  errorNome: string | null = null;
  errorEmail: string | null = null;
  errorSenha: string | null = null;
  errorTelefone: string | null = null;
  errorCargo: string | null = null;

  // --- Propriedades da Etapa 2 (ATUALIZADAS) ---
  cep: string = '';
  logradouro: string = '';
  numero: string = '';
  complemento: string = '';
  bairro: string = '';
  cidade: string = '';
  uf: string = '';
  pais: string = '';
  
  errorCep: string | null = null;
  errorLogradouro: string | null = null;
  errorNumero: string | null = null;
  errorBairro: string | null = null;
  errorCidade: string | null = null;
  errorUf: string | null = null;
  errorPais: string | null = null;

  isSubmitting = false;

  // --- MÉTODOS ---

  // Validações da Etapa 1
  validateNome = (nome: string) => !nome.trim() ? 'Nome é obrigatório.' : (nome.trim().length < 2 ? 'Nome deve ter pelo menos 2 caracteres.' : null);
  validateEmail = (email: string) => !email.trim() ? 'E-mail é obrigatório.' : (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email) ? 'E-mail inválido.' : null);
  validateSenha = (senha: string) => !senha ? 'Senha é obrigatória.' : (senha.length < 8 ? 'Senha deve ter pelo menos 8 caracteres.' : null);
  validateTelefone = (tel: string) => !tel.trim() ? 'Telefone é obrigatório.' : null;
  validateCargo = (cargo: string) => !cargo ? 'Selecione um cargo.' : null;

  // Validações da Etapa 2
  validateCampoObrigatorio = (campo: string, nome: string) => !campo.trim() ? `${nome} é obrigatório.` : (campo.trim().length < 2 ? `${nome} deve ter pelo menos 2 caracteres.` : null);
  validateUf = (uf: string) => !uf.trim() ? 'UF é obrigatório.' : (uf.trim().length !== 2 ? 'UF deve ter 2 caracteres.' : null);
  validateCep = (cep: string) => !cep.trim() ? 'CEP é obrigatório.' : null; // Validação simples, pode ser melhorada com regex

  onToggleSenha() {
    this.mostrarSenha = !this.mostrarSenha;
  }

  // Navegação entre etapas
  proximaEtapa() {
    this.errorNome = this.validateNome(this.nome);
    this.errorEmail = this.validateEmail(this.email);
    this.errorSenha = this.validateSenha(this.senha);
    this.errorTelefone = this.validateTelefone(this.telefone);
    this.errorCargo = this.validateCargo(this.cargo);

    if (!this.errorNome && !this.errorEmail && !this.errorSenha && !this.errorTelefone && !this.errorCargo) {
      this.etapa = 2; // Avança para a próxima etapa
    }
  }

  etapaAnterior() {
    this.etapa = 1; // Volta para a etapa anterior
  }

  // Submissão final do formulário
  onSubmit() {
    // Valida os campos da etapa 2
    this.errorCep = this.validateCep(this.cep);
    this.errorLogradouro = this.validateCampoObrigatorio(this.logradouro, 'Logradouro');
    this.errorNumero = !this.numero.trim() ? 'Número é obrigatório' : null;
    this.errorBairro = this.validateCampoObrigatorio(this.bairro, 'Bairro');
    this.errorCidade = this.validateCampoObrigatorio(this.cidade, 'Cidade');
    this.errorUf = this.validateUf(this.uf);
    this.errorPais = this.validateCampoObrigatorio(this.pais, 'País');

    // Verifica se há algum erro de validação antes de continuar
    if (this.errorCep || this.errorLogradouro || this.errorNumero || this.errorBairro || this.errorCidade || this.errorUf || this.errorPais) {
      return;
    }

    this.isSubmitting = true;
    const dadosCompletos = {
      // Dados da Etapa 1
      nome: this.nome,
      email: this.email,
      senha: this.senha,
      telefone: this.telefone,
      cargo: this.cargo,
      // Dados da Etapa 2
      cep: this.cep,
      logradouro: this.logradouro,
      numero: this.numero,
      complemento: this.complemento,
      bairro: this.bairro,
      cidade: this.cidade,
      uf: this.uf,
      pais: this.pais
    };

    setTimeout(() => {
      console.log('Cadastro Completo:', dadosCompletos);
      alert('Conta criada com sucesso!');
      this.isSubmitting = false;
    }, 1000);
  }
}