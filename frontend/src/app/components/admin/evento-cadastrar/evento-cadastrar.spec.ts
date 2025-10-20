import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventoCadastrar } from './evento-cadastrar';

describe('EventoCadastrar', () => {
  let component: EventoCadastrar;
  let fixture: ComponentFixture<EventoCadastrar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventoCadastrar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventoCadastrar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
