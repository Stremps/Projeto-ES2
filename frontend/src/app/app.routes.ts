import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Home } from './components/home/home';
import { Register1 } from './components/register1/register1';
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
        path: '**', redirectTo: 'login', pathMatch: 'full'
    }
];