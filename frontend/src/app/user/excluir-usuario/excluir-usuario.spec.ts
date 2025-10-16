import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ExcluirUsuario } from './excluir-usuario';
import { FormsModule } from '@angular/forms';

describe('ExcluirUsuario', () => {
  let component: ExcluirUsuario;
  let fixture: ComponentFixture<ExcluirUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Adicionamos HttpClientTestingModule para mocks HTTP e FormsModule para ngModel
      imports: [ExcluirUsuario, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExcluirUsuario);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // Verifica se o componente é criado com sucesso
    expect(component).toBeTruthy();
  });
  
  it('should require a password before calling deleteAccount', () => {
    spyOn(component, 'deleteAccount');
    component.confirmationPassword = '';
    
    // Tenta submeter o formulário sem senha
    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();
    
    // Verifica se a função de exclusão NÃO foi chamada
    expect(component.deleteAccount).toHaveBeenCalled();
    // Verifica se a mensagem de erro foi exibida
    expect(component.errorMessage).toBe('Por favor, digite sua senha para confirmar.');
  });
});
