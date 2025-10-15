import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExcluirUsuario } from './excluir-usuario';

describe('ExcluirUsuario', () => {
  let component: ExcluirUsuario;
  let fixture: ComponentFixture<ExcluirUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExcluirUsuario]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExcluirUsuario);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
