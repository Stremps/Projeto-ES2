import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-excluir-usuario',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './excluir-usuario.html',
  styleUrl: './excluir-usuario.css'
})
export class ExcluirUsuario implements OnInit {
  usuario: any = null;
  mensagem: string = '';
  erro: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.usuario = user;
    } else {
      this.router.navigate(['/login']);
    }
  }

  confirmarExclusao() {
    this.mensagem = 'Conta excluída com sucesso!';
    this.erro = false;

    setTimeout(() => {
      this.authService.logout();
      this.router.navigate(['/login']);
    }, 1500);
  }

  cancelar() {
    this.router.navigate(['/perfil-usuario']);
  }
}
