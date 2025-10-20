import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PalestraCadastrar } from './palestra-cadastrar';

describe('PalestraCadastrar', () => {
  let component: PalestraCadastrar;
  let fixture: ComponentFixture<PalestraCadastrar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PalestraCadastrar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PalestraCadastrar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
