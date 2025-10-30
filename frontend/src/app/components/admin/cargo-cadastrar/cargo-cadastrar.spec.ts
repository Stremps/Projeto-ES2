import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CargoCadastrar } from './cargo-cadastrar';

describe('CargoCadastrar', () => {
  let component: CargoCadastrar;
  let fixture: ComponentFixture<CargoCadastrar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CargoCadastrar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CargoCadastrar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
