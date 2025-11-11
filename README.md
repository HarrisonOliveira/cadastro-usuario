# Cadastro de Usuário

Projeto Spring Boot para CRUD de usuários com PostgreSQL, Docker Compose e boas práticas de organização (Controller → Service → Repository), DTOs e tratamento de exceções customizadas.

## Sumário
- [Visão Geral](#visão-geral)
- [Stack e Metodologias](#stack-e-metodologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração com application-secret.properties](#configuração-com-application-secretproperties)
- [Subindo as dependências com Docker Compose](#subindo-as-dependências-com-docker-compose)
- [Como rodar a aplicação](#como-rodar-a-aplicação)
- [Modelos de Requisição/Resposta](#modelos-de-requisiçãoresposta)
- [Dicas e Troubleshooting](#dicas-e-troubleshooting)

---

## Visão Geral
Este projeto expõe uma API REST para gerenciamento de usuários, permitindo:
- Cadastrar usuário
- Listar todos
- Buscar por ID
- Buscar por e‑mail
- Atualizar
- Deletar

A persistência é feita via Spring Data JPA em um banco PostgreSQL. O ambiente de banco e pgAdmin pode ser orquestrado por Docker Compose.

## Stack e Metodologias
- Linguagem/Frameworks:
  - Java + Spring Boot
  - Spring Web (REST)
  - Spring Data JPA
  - PostgreSQL
  - Lombok
  - SLF4J (logging)
  - Maven
  - Docker Compose
- Metodologias/Boas práticas:
  - Arquitetura em camadas: Controller → Service (regras de negócio) → Repository (persistência)
  - Uso de DTO (`UsuarioDTO`) para desacoplar a API da entidade
  - Exceptions customizadas (`IdUsuarioNaoEncontradoException`, `EmailNaoEncontradoException`)
  - Separação de configurações sensíveis em `application-secret.properties` e perfis do Spring
  - Uso de logs de aplicação para operações chave

## Estrutura do Projeto
Principais arquivos/pacotes:
- `src/main/java/com/praticando/cadastro_usuario/CadastroUsuarioApplication.java`: classe principal Spring Boot
- `controller/UsuarioController.java`: endpoints REST
- `bisness/UsuarioService.java`: regras de negócio
- `infractructure/repository/UsuarioRepository.java`: interface JPA
- `infractructure/entity/Usuario.java`: entidade JPA
- `infractructure/dto/UsuarioDTO.java`: DTO exposto na API
- `src/main/resources/application.properties`: config padrão
- `src/main/resources/application-secret.properties`: credenciais/segredos e variáveis para Docker
- `docker-compose.yaml`: serviços PostgreSQL e pgAdmin

## Configuração com application-secret.properties
Este projeto utiliza um arquivo extra para configuração das variáveis de ambiente na raiz do projeto: `src/main/resources/application-secret.properties`.

No `application.properties`:
```properties
spring.profiles.include=secret
spring.application.name=cadastro-usuario
server.port=8080
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.datasource.driver-class-name=org.postgresql.Driver
```
Ao incluir o profile `secret`, o Spring Boot automaticamente carrega `application-secret.properties` (sem precisar ativar explicitamente o profile na linha de comando).

O conteúdo esperado de `application-secret.properties` é:
```properties
# Credenciais do banco de dados usadas pela aplicação
spring.datasource.username=#usuário de acesso ao banco
spring.datasource.password=#senha de acesso ao banco
spring.datasource.url=jdbc:postgresql://localhost:5432/cadastro_usuario

# Variáveis usadas pelo docker-compose (env_file)
POSTGRES_DB=cadastro_usuario
POSTGRES_USER=#usuário de acesso ao banco
POSTGRES_PASSWORD=#senha de acesso ao banco
PGADMIN_DEFAULT_EMAIL=#email de acesso ao pgAdmin
PGADMIN_DEFAULT_PASSWORD=#senha de acesso ao pgAdmin
```

Observações importantes:
- É importante destacar que, como o `aplication-secret.properties` contem variaveis de ambiente ele deve ser incluído no `.gitignore`.
- O `docker-compose.yaml` por padrão consegue ler arquivos `.env`, porem para arquivos `.properties` usa-se `env_file: src/main/resources/application-secret.properties` para popular as variáveis dos containers.

## Subindo as dependências com Docker Compose
Pré‑requisitos:
- Docker Desktop instalado

Comandos na raiz do projeto:
```bash
  docker compose up -d
```
Isso iniciará:
- PostgreSQL em `localhost:5432`
- pgAdmin em `http://localhost:5050` (login/senha do `application-secret.properties`)

No pgAdmin, adicione um servidor apontando para `postgres` (ou `localhost`) na porta `5432`, banco `cadastro_usuario`, usuário e senha.

Para parar:
```bash
  docker compose down
```

## Como rodar a aplicação
Pré‑requisitos:
- JDK 17+ (ou a versão configurada no `pom.xml`)
- Maven (ou use os wrappers `mvnw`/`mvnw.cmd`)

Opção 1 — Maven (modo desenvolvimento):
```bash
  ./mvnw spring-boot:run      # Linux/MacOS
  mvn spring-boot:run         # Windows
```

Opção 2 — Empacotar e executar JAR:
```bash
  mvn clean package           # ou ./mvnw clean package
  java -jar target/cadastro-usuario-*.jar
```

A API subirá em `http://localhost:8080`.

## Endpoints
Base path: `/usuario`

- POST `http://localhost:8080/usuario/cadastrarUsuario`
  - Cadastra um usuário.
- GET `http://localhost:8080/usuario/getAll`
  - Retorna a lista de usuários.
- GET `http://localhost:8080//usuario/getById/{id}`
  - Busca um usuário por ID.
- GET `http://localhost:8080//usuario/getByEmail/{email}`
  - Busca um usuário por e‑mail.
- PUT `http://localhost:8080//usuario/update/{id}`
  - Atualiza nome/e‑mail do usuário pelo ID.
- DELETE `http://localhost:8080//usuario/deleteById/{id}`
  - Remove um usuário pelo ID.

## Modelos de Requisição/Resposta
DTO utilizado na API:
```json
{
  "nome": "string",
  "email": "string"
}
```

Respostas comuns:
- Criação (`POST /cadastrarUsuario`): 200 OK (sem body)
- Listagem e buscas (`GET`): 200 OK com JSON
- Atualização (`PUT`): 200 OK com o JSON enviado
- Exclusão (`DELETE`): 204 No Content

Erros possíveis:
- `404 Not Found` quando ID ou e‑mail não forem encontrados (`IdUsuarioNaoEncontradoException`, `EmailNaoEncontradoException`).

## Dicas e Troubleshooting
- Certifique‑se de que o PostgreSQL está no ar com as mesmas credenciais do `application-secret.properties`.
- Verifique a porta 8080 livre.
- Logs úteis são exibidos no console (SLF4J). Ex.: ao salvar ou remover um usuário.
- Se usar outro host/porta para o PostgreSQL (por ex., em container), ajuste `spring.datasource.url` adequadamente. No `docker-compose` padrão, o app acessa `localhost:5432`.

---

Feito com muito café. 🚀
