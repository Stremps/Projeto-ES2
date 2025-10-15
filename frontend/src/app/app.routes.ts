import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Home } from './components/home/home';
import { Register1 } from './components/register1/register1';
import { PerfilUsuario } from './components/perfil-usuario/perfil-usuario';
import { EditarUsuario } from './components/editar-usuario/editar-usuario';
import { ExcluirUsuario } from './components/excluir-usuario/excluir-usuario';
import { RouterLink } from '@angular/router';

export const routes: Routes = [
    {
        path: 'login',
        component: Login
    },
    {
        path: 'home',
        component: Home
    },
    {
        path: 'register1',
        component: Register1
    },
    {
        path: 'perfil-usuario',
        component: PerfilUsuario
    },
    {
        path: 'editar-usuario',
        component: EditarUsuario
    },
    {
        path: 'excluir-usuario',
        component: ExcluirUsuario
    },
    {
        path: '**', redirectTo: 'login', pathMatch: 'full'
    }
];