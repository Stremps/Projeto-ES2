import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Register1 } from './register1';

describe('Register1', () => {
  let component: Register1;
  let fixture: ComponentFixture<Register1>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Register1]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Register1);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
