import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-evento',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './evento.html',
  styleUrl: './evento.css'
})
export class Evento {
  @Input() event: any;
}
