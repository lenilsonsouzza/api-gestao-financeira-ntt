# 🚀 MS1 - Microsserviço de Gestão e Autenticação de Utilizadores

Microsserviço responsável pela gestão completa do ciclo de vida dos Usuários, autenticação segura via JSON Web Token (JWT) e fornecimento de dados cadastrais essenciais para o ecossistema de gestão financeira.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

* **Java 25** / **Spring Boot 3.x**
* **Spring Security** & **JWT (JSON Web Token)** para autenticação e segurança.
* **Spring Data JPA** / **Hibernate** para persistência de dados e mapeamento ORM.
* **Banco de Dados Relacional** (PostgreSQL / H2 para testes).
* **SpringDoc OpenAPI (Swagger)** para documentação interativa da API.
* **JUnit 5** & **Mockito** para suíte de testes unitários e de integração.
* **Lombok** para redução de código boilerplate.
* **Maven** para gestão de dependências e build.

---

## 🏛️ Arquitetura e Organização do Projeto

O projeto segue os princípios da **Arquitetura Limpa (Clean Architecture)** e **Arquitetura Hexagonal (Ports and Adapters)**, garantindo total desacoplamento entre as regras de negócio de domínio e os detalhes de infraestrutura:

```text
src/
├── main/java/com/financeiro/ms1usuarios/
│   ├── application/          # Portas de Entrada/Saída (Inbound/Outbound Ports) e Casos de Uso
│   ├── domain/               # Entidades de Domínio, Exceções e Regras de Negócio Puras
│   └── infrastructure/       # Controladores REST, Adapters de Repositório JPA, Segurança (JWT) e DTOs
└── test/                     # Suíte de testes unitários e de integração
```

---

## 📋 Regras de Negócio Principais

1. **Gestão de Utilizadores:**
   * Registo de novos utilizadores com encriptação segura de passwords via `PasswordEncoder`.
   * Atualização e consulta de perfis de utilizador por ID ou E-mail.
   * Eliminação segura de contas.

2. **Segurança e Controlo de Acesso:**
   * Validação de propriedade através de `SecurityUtils.verifyIsOwner(id)`, garantindo que utilizadores apenas possam alterar ou eliminar os seus próprios dados.

3. **Autenticação JWT:**
   * Endpoint `/auth/login` para validação de credenciais (e-mail e palavra-passe).
   * Geração e emissão de tokens JWT assinados para autorização em pedidos subsequentes.

---

## 🧪 Relatório de Testes

A camada de domínio e os fluxos de segurança possuem cobertura de testes automatizados com **JUnit 5** e **Mockito**:

* **Casos de Uso de Utilizador:** Validação dos fluxos de criação, busca, atualização e remoção.
* **Autenticação & Segurança:** Cobertura para credenciais inválidas, emissão de tokens e bloqueio de acessos não autorizados.

Para executar a suíte de testes via terminal:
```bash
mvn clean test
```

---

## 📚 Documentação da API (Swagger / OpenAPI)

Com a aplicação em execução, a documentação interativa da API pode ser acedida em:
* **URL do Swagger:** `http://localhost:8080/swagger-ui/index.html`

### Endpoints Principais:
* `POST /auth/login` - Autenticação e obtenção do token JWT.
* `POST /api/users` - Registo de novo utilizador.
* `GET /api/users` - Listagem de utilizadores.
* `GET /api/users/{id}` - Busca de utilizador por ID.
* `PUT /api/users/{id}` - Atualização de dados de utilizador.
* `DELETE /api/users/{id}` - Remoção de utilizador.