import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscreverUsuario } from './inscrever-usuario';

describe('InscreverUsuario', () => {
  let component: InscreverUsuario;
  let fixture: ComponentFixture<InscreverUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscreverUsuario]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscreverUsuario);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
