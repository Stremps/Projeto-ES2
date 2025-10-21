import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventoEditar } from './evento-editar';

describe('EventoEditar', () => {
  let component: EventoEditar;
  let fixture: ComponentFixture<EventoEditar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventoEditar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventoEditar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
