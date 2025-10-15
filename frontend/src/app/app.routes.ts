import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { PaginaInicial } from './components/pagina-inicial/pagina-inicial';
import { Register1 } from './components/register1/register1';
import { RouterLink } from '@angular/router';
import { HomeComponent } from './components/home/home';
import { PerfilUsuario } from './user/perfil-usuario/perfil-usuario';
import { EditarUsuario } from './user/editar-usuario/editar-usuario';

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
    {
<<<<<<< HEAD
        path: 'home',
        component: HomeComponent
=======
        path: 'user',
        component: PerfilUsuario
    },
    {
        path: 'edit-user',
        component: EditarUsuario
>>>>>>> 2114e61 (criação de tela de perfil)
    },
    {
        path: '**', redirectTo: 'pagina-inicial', pathMatch: 'full'
    }
];