import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaInicial } from './pagina-inicial';

describe('PaginaInicial', () => {
  let component: PaginaInicial;
  let fixture: ComponentFixture<Home>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaginaInicial]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaginaInicial);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
