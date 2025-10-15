import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { PaginaInicial } from './components/pagina-inicial/pagina-inicial';
import { Register1 } from './components/register1/register1';
import { RouterLink } from '@angular/router';
import { HomeComponent } from './components/home/home';

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
        path: 'home',
        component: HomeComponent
    },
    {
        path: '**', redirectTo: 'pagina-inicial', pathMatch: 'full'
    }
];