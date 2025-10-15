import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-perfil-usuario',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './perfil-usuario.html',
  styleUrl: './perfil-usuario.css'
})
export class PerfilUsuario implements OnInit {
  usuario: any = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarPerfil();
  }

  carregarPerfil() {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.usuario = user;
    } else {
      this.router.navigate(['/login']);
    }
  }

  voltarHome() {
    this.router.navigate(['/home']);
  }

  editarPerfil() {
    this.router.navigate(['/editar-usuario']);
  }
}
