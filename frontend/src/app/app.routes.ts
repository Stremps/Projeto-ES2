import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { PaginaInicial } from './components/pagina-inicial/pagina-inicial';
import { Register1 } from './components/register1/register1';
import { HomeComponent } from './components/home/home';
import { PerfilUsuario } from './user/perfil-usuario/perfil-usuario';
import { EditarUsuario } from './user/editar-usuario/editar-usuario';
import { ExcluirUsuario } from './user/excluir-usuario/excluir-usuario';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
    {
        path: 'login',
        component: Login
    },
    {
        path: 'pagina-inicial',
        component: PaginaInicial
    },
    {
        path: 'register1',
        component: Register1
    },
    // A PARTIR DAQUI, TODOS AS ROTAS SERAO PROTEGIDAS PELO GUARD
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [authGuard]
    },
    {
        path: 'user',
        component: PerfilUsuario,
        canActivate: [authGuard]
    },
    {
        path: 'edit-user',
        component: EditarUsuario,
        canActivate: [authGuard]
    },
    {
        path: 'delete-user',
        component: ExcluirUsuario,
        canActivate: [authGuard]
    },
    {
        path: '**', redirectTo: 'pagina-inicial', pathMatch: 'full'
    }
];