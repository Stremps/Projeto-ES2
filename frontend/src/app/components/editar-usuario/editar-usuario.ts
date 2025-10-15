import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-editar-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './editar-usuario.html',
  styleUrl: './editar-usuario.css'
})
export class EditarUsuario implements OnInit {
  email: string = '';
  novaSenha: string = '';
  confirmarSenha: string = '';
  mensagem: string = '';
  erro: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.email = user.email;
    } else {
      this.router.navigate(['/login']);
    }
  }

  salvar() {
    this.mensagem = '';
    this.erro = false;

    if (this.novaSenha && this.novaSenha !== this.confirmarSenha) {
      this.mensagem = 'As senhas não coincidem';
      this.erro = true;
      return;
    }

    if (this.novaSenha && this.novaSenha.length < 6) {
      this.mensagem = 'A senha deve ter no mínimo 6 caracteres';
      this.erro = true;
      return;
    }

    this.mensagem = 'Perfil atualizado com sucesso!';
    this.erro = false;

    setTimeout(() => {
      this.router.navigate(['/perfil-usuario']);
    }, 1500);
  }

  cancelar() {
    this.router.navigate(['/perfil-usuario']);
  }
}
