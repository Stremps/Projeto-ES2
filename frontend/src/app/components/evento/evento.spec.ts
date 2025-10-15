import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Evento } from './evento';

describe('Evento', () => {
  let component: Evento;
  let fixture: ComponentFixture<Evento>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Evento]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Evento);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
