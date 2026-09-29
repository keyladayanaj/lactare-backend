# Lactare — preparação para teste local

## Ambiente esperado

- Java 25
- Maven Wrapper 3.9.16 (o projeto usa `mvnw.cmd`)
- Front-end servido por Live Server, preferencialmente em `http://localhost:5500`
- Back-end em `http://localhost:8080`

## Back-end

No Windows PowerShell, dentro de `backend`:

```powershell
mvnw.cmd clean test
mvnw.cmd spring-boot:run
```

Perfil ativo: `test`.

Banco: H2 em memória.

Console H2:

```text
http://localhost:8080/h2-console
```

JDBC:

```text
jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
```

As credenciais da Equipe de teste ficam em `application-test.properties` e não estão hardcoded no código Java.

## Credenciais de teste

### Doadora

```text
usuario: nutriz_maria
senha: senha123
```

### Rede de Apoio

```text
usuario: familia_oliveira
senha: senha123
```

### Equipe Lactare

```text
usuario: equipe_lactare
senha: Lactare@2026
```

As duas primeiras são dados de teste do `import.sql`; a credencial da equipe pertence somente ao perfil `test`.

## Front-end

Abra a pasta `frontend` com Live Server.

Preferencialmente:

```text
http://localhost:5500/index.html
```

O cliente centralizado da API está em:

```text
frontend/assets/js/api-client.js
```

## Endpoints principais

### Autenticação

```text
POST /api/v1/auth/login
POST /api/v1/auth/logout
```

### Doadora

```text
POST /api/v1/doadoras
GET  /api/v1/doadoras
GET  /api/v1/doadoras/{id}
```

### Rede de Apoio

```text
POST /api/v1/apoios
GET  /api/v1/apoios
GET  /api/v1/apoios/{id}
```

### Triagem

```text
POST  /api/v1/triagens
GET   /api/v1/triagens
GET   /api/v1/triagens/{id}
GET   /api/v1/triagens/doadora/{id}
PATCH /api/v1/triagens/{id}/status
```

### Exame médico usando Consulta

```text
POST /api/v1/consultas/exame-medico
GET  /api/v1/consultas/doadora/{id}/exame-medico
PUT  /api/v1/consultas/{id}/aprovar-exame
```

### Coleta

```text
POST /api/v1/coletas/agendamento
GET  /api/v1/coletas/doadora/{id}
GET  /api/v1/coletas
```

## Fluxo principal para teste

```text
Cadastro da Doadora
→ Login
→ Envio da Triagem
→ Triagem PENDENTE
→ Equipe visualiza
→ Equipe aprova
→ Doadora recebe APROVADA
→ Exame médico disponível
→ Agendamento do exame
→ Equipe aprova exame
→ Coleta liberada
→ Agendamento da coleta
→ Equipe visualiza coleta
```

## Testes negativos

Verificar que o Back-end bloqueia:

1. coleta antes da aprovação da triagem/exame;
2. aprovação de triagem sem perfil EQUIPE;
3. aprovação de exame sem perfil EQUIPE;
4. acesso de uma Doadora aos dados de outra Doadora;
5. acesso a endpoints protegidos sem Bearer token;
6. criação de consulta genérica por perfil não autorizado;
7. criação/alteração direta de coleta por perfil não autorizado.

## Observação de validação

A validação final de runtime depende de executar o Maven Wrapper com Java 25 no computador do projeto. No ambiente de análise não há JDK 25 nem Maven 3.9.16 baixado localmente, e o download do Maven Wrapper não pôde ser concluído.
