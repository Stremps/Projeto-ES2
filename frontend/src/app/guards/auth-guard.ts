import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  // Verifica se o usuário está logado usando o serviço
  if (authService.isUserLoggedIn()) {
    return true; // Permite o acesso à rota
  } else {
    // Se não estiver logado, redireciona para a página de login
    router.navigate(['/login']);
    return false; // Bloqueia o acesso à rota
  }
};
