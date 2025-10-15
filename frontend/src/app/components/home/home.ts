import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  irParaPerfil() {
    this.router.navigate(['/perfil-usuario']);
  }

  editarPerfil() {
    this.router.navigate(['/editar-usuario']);
  }

  excluirConta() {
    this.router.navigate(['/excluir-usuario']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
