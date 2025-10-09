import { Component, signal } from '@angular/core';
import { provideRouter, RouterModule, RouterOutlet } from '@angular/router';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { Card } from "primeng/card";
import { bootstrapApplication } from '@angular/platform-browser';
import { routes } from './app.routes';
import { SelectModule } from 'primeng/select';
import { provideAnimations } from '@angular/platform-browser/animations';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CardModule, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App {
  protected readonly title = signal('trabalho1');
}