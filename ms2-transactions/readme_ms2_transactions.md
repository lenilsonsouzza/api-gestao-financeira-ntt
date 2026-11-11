# 💳 MS2 - Microsserviço de Transações, Câmbio e Análise Financeira

Microsserviço de elevada performance responsável pelo processamento de transações financeiras (depósitos, levantamentos, compras e transferências), integração com serviços de câmbio em tempo real, relatórios analíticos e exportação de dados em formato CSV.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

* **Java 25** / **Spring Boot 3.x**
* **Spring Cloud / OpenFeign** para comunicação síncrona com o `ms1-usuarios` (validação de utilizadores e saldos).
* **Spring Data JPA** / **Hibernate** para persistência e gestão relacional de dados.
* **Spring Security & JWT** para extração e validação do contexto de segurança do utilizador autenticado (`SecurityContextHolder`).
* **Clients Externos de Câmbio** (ex: Brasil API / Serviços de Cotação) para conversão de moedas em tempo real.
* **SpringDoc OpenAPI (Swagger)** para documentação interativa das APIs.
* **JUnit 5** & **Mockito** para suíte de testes unitários isolados da camada de domínio.
* **Maven** para build e gestão de pacotes.

---

## 🌐 Integrações e APIs Externas

Este microsserviço consome APIs externas e serviços auxiliares para enriquecer as transações e simular integrações reais do ecossistema:

* **Brasil API (Cotação de Câmbio):** Consumida para obter cotações de moedas em tempo real (ex: USD, EUR) no use case de conversão (`GetTransactionWithExchangeUseCase`).
    * 🔗 [Documentação Brasil API](https://brasilapi.com.br/)
* **MockAPI:** Utilizada para simulação de webhooks/endpoints externos durante a validação de fluxos e testes de integração.
    * 🔗 [Painel MockAPI](https://mockapi.io/)
---


## 🏛️ Arquitetura e Organização do Projeto

Desenvolvido segundo a **Clean Architecture (Ports and Adapters)**, isolando completamente as regras de negócio de transação das bibliotecas e frameworks externos:

```text
src/
├── main/java/com/gestaofinanceira/ms2transactions/
│   ├── domain/               # Entidades de Domínio, Use Cases, Portas In/Out e Exceções
│   └── infrastructure/       # Controllers REST, Clientes Feign, Adapters JPA, DTOs e Exportador CSV
└── test/                     # Suíte de testes unitários focada nos Use Cases com Mockito
```

---

## 💡 Regras de Negócio e Integrações

1. **Criação e Validação de Transações (`CreateTransactionUseCase`):**
   * Comunicação via Feign com o `ms1-usuarios` para validar se o utilizador remetente/destino existe e está ativo no sistema.
   * Validação de regras operacionais: verificação de saldos e limites permitidos por transação.
   * Transações aprovadas geram atualização de saldo e persistência com estado `APPROVED`; transações inválidas são registadas com o estado `REJECTED` e o respetivo motivo.

2. **Cotação de Câmbio Dinâmica (`GetTransactionWithExchangeUseCase`):**
   * Permite consultar detalhes de uma transação enriquecida com cotações de moedas estrangeiras (USD, EUR).
   * **Garantia de Segurança:** Valida se a transação pertence ao utilizador autenticado antes de consultar serviços externos. Se a transação não pertencer ao utilizador, lança exceção de acesso negado.

3. **Análise Financeira e Exportação CSV (`ExpenseAnalysisUseCase`):**
   * Agrupamento inteligente de despesas por categoria, dia e mês em períodos configuráveis.
   * Endpoint de download `/transactions/analysis/csv` que gera relatórios no formato `.csv` codificado em UTF-8.

---

## 🧪 Relatório de Testes (Unitários & Cobertura)

A suíte de testes unitários cobre com rigor os casos de uso principais e situações de exceção:

* **`CreateTransactionUseCaseTest`:**
  * `deveCriarDepósitoComSucesso()`: Valida crédito de saldo e aprovação de transação.
  * `deveRejeitarTransacaoQuandoUsuarioNaoExiste()`: Confirma o bloqueio quando a validação remota falha.
  * `deveRejeitarTransacaoQuandoLimiteExcedido()`: Garante a rejeição quando a transação excede o limite estabelecido.
  * `deveCriarOutroDepositoComSucesso()`: Confirma a consistência dos fluxos aprovados.

* **`GetTransactionWithExchangeUseCaseTest`:**
  * `deveRetornarTransacaoComConversaoQuandoAprovada()`: Valida o retorno com conversões de câmbio (USD/EUR).
  * `deveLancarExcecaoQuandoTransacaoNaoPertenceAoUsuario()`: Valida o bloqueio de segurança e verifica que o serviço externo de câmbio não é invocado.

Para executar os testes via terminal:
```bash
mvn clean test
```

---

## 📚 Documentação da API (Swagger / OpenAPI)

Com o microsserviço a correr localmente, podes consultar e testar todos os endpoints interativamente através da interface do Swagger:
* **URL do Swagger:** `http://localhost:8081/swagger-ui/index.html` (ou porta configurada)

### Endpoints Principais:
* `POST /transactions` - Criação de transações (Depósito, Levantamento, Compra, Transferência).
* `GET /transactions/{id}/exchange` - Consulta de transação enriquecida com cotação de câmbio.
* `GET /transactions/analysis` - Análise consolidada de despesas por período.
* `GET /transactions/analysis/csv` - Exportação do relatório de despesas em formato CSV.
* `GET /transactions/balance` - Consulta de saldo atualizado da conta.

