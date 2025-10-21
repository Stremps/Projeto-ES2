import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PalestraEditar } from './palestra-editar';

describe('PalestraEditar', () => {
  let component: PalestraEditar;
  let fixture: ComponentFixture<PalestraEditar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PalestraEditar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PalestraEditar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
