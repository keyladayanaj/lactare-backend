LACTECH - API REST

Projeto: Lactare

Tecnologia: Java / Spring Boot

Tipo de aplicação: API REST

Equipe: Lactech

INTEGRANTES

Beatriz Matos Marques Rosa - RM: 554805

Keyla Dayana Magne Ala - RM: 557630

Lara Rosa Sodré - RM: 555496

SOBRE O PROJETO

O Lactare é uma solução desenvolvida para apoiar o gerenciamento e a organização de atividades relacionadas à doação de leite materno.

Nesta Sprint 3, foi desenvolvida uma API REST responsável por disponibilizar os dados e funcionalidades da solução por meio de endpoints, permitindo o gerenciamento de informações relacionadas às doadoras, rede de apoio, profissionais, coletas, consultas, avaliações e informações complementares.

A API possui persistência de dados, validação das informações recebidas, tratamento de exceções, documentação dos endpoints e organização em camadas.

OBJETIVO

O objetivo desta API é representar a solução definida nas Sprints anteriores, disponibilizando uma estrutura organizada para gerenciamento das informações relacionadas ao processo de doação de leite materno.

A aplicação permite realizar operações de criação, consulta, atualização e exclusão dos principais recursos da solução, mantendo a separação de responsabilidades entre as diferentes camadas da aplicação.

FUNCIONALIDADES

A API disponibiliza operações para:

Cadastro e gerenciamento de doadoras;
Cadastro e gerenciamento de pessoas da rede de apoio;
Cadastro e gerenciamento de profissionais;
Registro e gerenciamento de coletas;
Agendamento e gerenciamento de consultas;
Registro e gerenciamento de avaliações;
Cadastro e gerenciamento de informações complementares das doadoras;
Consulta de registros individuais;
Atualização de registros;
Exclusão de registros;
Validação dos dados recebidos;
Tratamento de erros e recursos não encontrados.
ENTIDADES

A aplicação possui as seguintes entidades:

Doadora

Representa a pessoa responsável pela doação de leite materno, contendo informações cadastrais e seus relacionamentos com coletas, consultas, avaliações e informações complementares.

Apoio

Representa os usuários da rede de apoio, contendo informações cadastrais e relacionamentos com consultas e avaliações.

Profissional

Representa os profissionais envolvidos no acompanhamento das atividades, podendo estar relacionados a consultas e avaliações.

Coleta

Representa os registros de coleta de leite materno, contendo informações como data, volume, status e observações.

Consulta

Representa os agendamentos e atendimentos realizados, contendo data, horário, motivo, informações adicionais e status.

Avaliação

Representa as avaliações relacionadas aos atendimentos, contendo nota, comentário, data e relacionamentos com os participantes da consulta.

Informação

Representa informações complementares relacionadas à doadora, incluindo dados sobre amamentação, medicamentos, método de coleta, alergias, doenças, hábitos e observações.

OPERAÇÕES DA API

Os endpoints da aplicação seguem o padrão REST e utilizam versionamento por meio do prefixo /api/v1.

Doadoras

GET /api/v1/doadoras

Consulta todas as doadoras cadastradas.

GET /api/v1/doadoras/{id}

Consulta uma doadora específica pelo ID.

POST /api/v1/doadoras

Cadastra uma nova doadora.

PUT /api/v1/doadoras/{id}

Atualiza os dados de uma doadora.

DELETE /api/v1/doadoras/{id}

Remove uma doadora.

Apoios

GET /api/v1/apoios

Consulta todos os registros de apoio.

GET /api/v1/apoios/{id}

Consulta um registro de apoio específico.

POST /api/v1/apoios

Cadastra um novo registro de apoio.

PUT /api/v1/apoios/{id}

Atualiza um registro de apoio.

DELETE /api/v1/apoios/{id}

Remove um registro de apoio.

Profissionais

GET /api/v1/profissionais

Consulta todos os profissionais cadastrados.

GET /api/v1/profissionais/{id}

Consulta um profissional específico.

POST /api/v1/profissionais

Cadastra um novo profissional.

PUT /api/v1/profissionais/{id}

Atualiza um profissional.

DELETE /api/v1/profissionais/{id}

Remove um profissional.

Coletas

GET /api/v1/coletas

Consulta todas as coletas cadastradas.

GET /api/v1/coletas/{id}

Consulta uma coleta específica.

POST /api/v1/coletas

Registra uma nova coleta.

PUT /api/v1/coletas/{id}

Atualiza uma coleta.

DELETE /api/v1/coletas/{id}

Remove uma coleta.

Consultas

GET /api/v1/consultas

Consulta todos os agendamentos cadastrados.

GET /api/v1/consultas/{id}

Consulta uma consulta específica.

POST /api/v1/consultas

Cadastra uma nova consulta.

PUT /api/v1/consultas/{id}

Atualiza uma consulta.

DELETE /api/v1/consultas/{id}

Remove uma consulta.

Avaliações

GET /api/v1/avaliacoes

Consulta todas as avaliações cadastradas.

GET /api/v1/avaliacoes/{id}

Consulta uma avaliação específica.

POST /api/v1/avaliacoes

Cadastra uma nova avaliação.

PUT /api/v1/avaliacoes/{id}

Atualiza uma avaliação.

DELETE /api/v1/avaliacoes/{id}

Remove uma avaliação.

Informações

GET /api/v1/informacoes

Consulta todas as informações cadastradas.

GET /api/v1/informacoes/{id}

Consulta uma informação específica.

POST /api/v1/informacoes

Cadastra uma nova informação.

PUT /api/v1/informacoes/{id}

Atualiza uma informação.

DELETE /api/v1/informacoes/{id}

Remove uma informação.

ARQUITETURA DO PROJETO

A aplicação foi organizada em camadas para separar as responsabilidades e evitar o acesso direto dos Controllers à camada de persistência.

A estrutura principal do projeto é:

controller — responsável pelos endpoints da API e recebimento das requisições;

service — responsável pelas regras e operações da aplicação;

repositories — responsável pelo acesso e persistência dos dados;

entities — contém as entidades utilizadas para representar os dados persistidos;

dto — contém as estruturas específicas para entrada e saída de dados;

exceptions — contém as exceções e o tratamento global de erros.

Essa organização permite uma maior separação de responsabilidades e facilita a manutenção e evolução da aplicação.

VALIDAÇÃO E TRATAMENTO DE ERROS

A aplicação utiliza recursos de validação do Spring para verificar os dados recebidos pelas requisições.

Os dados são recebidos por meio de DTOs, evitando a exposição direta das entidades persistidas.

A aplicação também possui tratamento global de exceções, incluindo situações como:

Dados inválidos;
Recurso não encontrado;
Erros relacionados ao banco de dados.
PERSISTÊNCIA DE DADOS

A aplicação utiliza Spring Data JPA para realizar a persistência dos dados.

Para execução e testes, foi configurado o banco de dados H2 em memória, permitindo que a aplicação seja executada sem a necessidade de instalar um banco de dados externo.

Os dados iniciais utilizados para testes são carregados por meio do arquivo import.sql.

DOCUMENTAÇÃO DA API

A API possui documentação utilizando OpenAPI/Swagger, permitindo visualizar e testar os endpoints diretamente pelo navegador.

Após iniciar a aplicação, a documentação pode ser acessada em:

http://localhost:8080/swagger-ui/index.html

A interface permite consultar os endpoints disponíveis, visualizar os parâmetros necessários e realizar testes das requisições.

TECNOLOGIAS UTILIZADAS
Java 25
Spring Boot
Spring Web MVC
Spring Data JPA
Spring Validation
H2 Database
OpenAPI / Swagger
Maven
Lombok
ESTRUTURA DO PROJETO

A aplicação está organizada da seguinte forma:

src/main/java/com/github/lara/sodre/ms_lactech/controller — Controllers e endpoints da API;

src/main/java/com/github/lara/sodre/ms_lactech/service — regras e operações da aplicação;

src/main/java/com/github/lara/sodre/ms_lactech/repositories — persistência e acesso ao banco de dados;

src/main/java/com/github/lara/sodre/ms_lactech/entities — entidades da aplicação;

src/main/java/com/github/lara/sodre/ms_lactech/dto — objetos de entrada e saída;

src/main/java/com/github/lara/sodre/ms_lactech/exceptions — tratamento de exceções;

src/main/resources — arquivos de configuração e dados iniciais.

COMO EXECUTAR O PROJETO
Pré-requisitos

Para executar o projeto, é necessário ter instalado:

Java JDK 25;
Maven ou utilizar o Maven Wrapper disponibilizado no projeto;
Git;
Uma IDE como IntelliJ IDEA, Eclipse ou Visual Studio Code.
1. Clonar o repositório

Clone o repositório do projeto:

git clone COLE_AQUI_O_LINK_DO_GITHUB

2. Entrar na pasta do projeto

cd ms-lactech

3. Executar a aplicação

No Windows, utilizando o Maven Wrapper:

mvnw.cmd spring-boot:run

Ou, caso o Maven esteja instalado:

mvn spring-boot:run

4. Acessar a API

Após a inicialização da aplicação, a API estará disponível em:

http://localhost:8080

5. Acessar a documentação Swagger

A documentação interativa estará disponível em:

http://localhost:8080/swagger-ui/index.html

6. Acessar o banco H2

O console do banco de dados está disponível em:

http://localhost:8080/h2-console

Para conexão utilizando a configuração de testes:

JDBC URL: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1

User Name: 554805

Password: 151005

COMO TESTAR A API

Os endpoints podem ser testados diretamente pela interface do Swagger ou por ferramentas como Postman e Insomnia.

Exemplo de consulta de todas as doadoras:

GET http://localhost:8080/api/v1/doadoras

Exemplo de consulta de uma doadora:

GET http://localhost:8080/api/v1/doadoras/1

Os demais recursos seguem o mesmo padrão de endpoints apresentado na seção Operações da API.

VERSIONAMENTO

As rotas da API utilizam versionamento por meio do prefixo:

/api/v1

Esse padrão permite que futuras versões da API possam ser disponibilizadas sem comprometer a compatibilidade com a versão atual.

BANCO DE DADOS

Durante a execução, a aplicação utiliza o banco H2 em memória.

A estrutura das tabelas é criada automaticamente pelo JPA e os dados de teste são disponibilizados por meio do arquivo import.sql.

Dessa forma, o projeto pode ser executado e testado sem configuração de um banco de dados externo.