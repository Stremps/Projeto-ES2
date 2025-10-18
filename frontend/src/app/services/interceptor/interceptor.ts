import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Auth } from '../auth';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private authService = inject(Auth);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Pega o token do serviço de autenticação
    const authToken = this.authService.getToken();

    // Clona a requisição para adicionar o novo cabeçalho
    // Isso garante que a requisição original não seja modificada
    let authReq = req;

    // Se o token existir e a URL não for a de login/registro (para evitar enviar token desnecessariamente)
    if (authToken && !req.url.includes('/api/auth/')) {
      authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${authToken}`)
      });
    }

    // Envia a requisição (original ou a clonada com o cabeçalho) para o próximo handler
    return next.handle(authReq);
  }
}
