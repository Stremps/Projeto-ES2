import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { EditarUsuario } from './editar-usuario';
import { FormsModule } from '@angular/forms'; // Adicionado para simular o módulo no teste

describe('EditarUsuario', () => {
  let component: EditarUsuario;
  let fixture: ComponentFixture<EditarUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Adicionamos HttpClientTestingModule para mocks HTTP e FormsModule para ngModel
      imports: [EditarUsuario, HttpClientTestingModule, FormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarUsuario);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // Verifica se o componente é criado com sucesso
    expect(component).toBeTruthy();
  });

  it('should load mock user data on initialization', () => {
    // Verifica se os dados mockados são carregados
    expect(component.user.nomeCompleto).toBe('João Silva');
    expect(component.user.email).toBe('JoaoSilva@email.com');
  });
  
  // Teste para verificar se o botão Salvar chama a função saveProfile (simulado)
  it('should call saveProfile when form is submitted', () => {
    spyOn(component, 'saveProfile');
    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();
    expect(component.saveProfile).toHaveBeenCalled();
  });
});
