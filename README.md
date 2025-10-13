# Projeto-ES2

---

## Detalhes técnicos

### Equipe:

- Aliana Wakassugui de Paula e Silva (Scrum Master);
- Jamile Hassen Sa;
- José Lucas Hoppe Macedo;
- Gustavo Camargo Domingues.

### Tecnologias Utilizadas:

Para o frontend, utilizará Angular.js e Node.js para interfaces interativas e formulários dinâmicos. No backend, Spring Boot ou Node.js será adotado para processamento de cadastros, validação e envio de e-mails. O banco de dados PostgreSQL garantirá armazenamento escalável de dados de participantes. Para automação de CI/CD (Integração Contínua/Entrega Contínua), será utilizado o GitHub Actions, garantindo a construção, teste e deploy automáticos do projeto. Além disso, Docker será empregado para a conteinerização da aplicação, facilitando o desenvolvimento, teste e implantação em diferentes ambientes.

---

## Sobre o Docker:

### Serviços definidos:

- `database`: O banco de dados PostgreSQL.
- `backend`: API Spring Boot
- `frontend-prod`: Aplicação Angular no modo de produção (otimizada e servida por um web server)
- `frontend-dev`: Aplicação Angular no modo de desenvolvimento (com update automático)

### Comando para cenários:

#### A) Ambiente Completo (BD, Backend e Frontend de Produção)

````
docker compose up -d --build database backend frontend-prod
````

#### B) Desenvolvimento do Frontend

````
docker compose watch frontend-dev
````

#### C) Apenas Backend e Banco de Dados

````
docker compose up -d --build database backend
````


---

## Sprint 1:

Sistema de cadastro (com formulário) de participantes.
