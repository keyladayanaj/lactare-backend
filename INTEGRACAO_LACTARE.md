# Integração real do Lactare

## Arquitetura

Front-end HTML/CSS/JS → API REST Spring Boot → H2/JPA.

O Front-end mantém o layout existente e concentra as chamadas HTTP em `assets/js/api-client.js`.

## Endpoints principais

### Autenticação
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`

### Doadora
- `POST /api/v1/doadoras`
- `GET /api/v1/doadoras/{id}`

### Triagem
- `POST /api/v1/triagens`
- `GET /api/v1/triagens`
- `GET /api/v1/triagens/{id}`
- `GET /api/v1/triagens/doadora/{doadoraId}`
- `PATCH /api/v1/triagens/{id}/status`

### Exame médico usando Consulta
- `POST /api/v1/consultas/exame-medico`
- `GET /api/v1/consultas/doadora/{doadoraId}/exame-medico`
- `PUT /api/v1/consultas/{id}/aprovar-exame`

### Coleta
- `POST /api/v1/coletas/agendamento`
- `GET /api/v1/coletas/doadora/{doadoraId}`
- CRUD existente em `/api/v1/coletas`

## Regras de negócio

1. Triagem criada começa como `PENDENTE`.
2. A equipe altera a triagem para `EM_ANALISE`, `APROVADA` ou `NAO_APROVADA`.
3. Exame médico só pode ser agendado com triagem `APROVADA`.
4. Coleta só pode ser agendada com exame médico `APROVADA`.
5. O Front-end não usa localStorage como fonte de dados da triagem/coleta/exame.
6. localStorage é usado para token e sessão do usuário.
7. A autenticação de sessão é enviada como `Authorization: Bearer <token>`.
8. CORS está configurado para execução local do Front-end via portas 5500/5501.

## Execução

### Back-end

```text
cd ms-lactech-main
mvn spring-boot:run
```

ou, no Windows:

```text
mvnw.cmd spring-boot:run
```

API: `http://localhost:8080`
Swagger: `http://localhost:8080/swagger-ui/index.html`
H2: `http://localhost:8080/h2-console`

### Front-end

Use Live Server ou outro servidor HTTP local, preferencialmente na porta 5500.

Não abra as páginas HTML diretamente via `file://`, pois o navegador pode bloquear as chamadas CORS/fetch.
