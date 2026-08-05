---
name: numisis-api
description: Referência da API REST do Numisis (Spring Boot) — endpoints, DTOs, envelopes de resposta, autenticação e regras de negócio de cada entidade. Use ao construir ou integrar telas do frontend (chamadas HTTP, tipos/interfaces, tratamento de erro) para Aluno, Professor, Disciplina, Turma, Curso, Matricula, HistoricoDisciplina, Endereco, Telefone, DadosBancarios, Usuario, Pessoa e Auth.
---

# API Numisis — Guia de referência

Esta skill documenta a API real do backend (`com.FVSS.numisis`), com base no código dos `controller`, `service`, `dto`, `mapper` e `domain.model`. O objetivo é dar contexto suficiente para gerar chamadas de frontend corretas (tipos, payloads, tratamento de erro) sem precisar reler o backend a cada tarefa.

Consulte o arquivo da entidade específica em `entities/` quando for implementar uma tela ou integração. Este arquivo cobre apenas as convenções **compartilhadas** por todos os endpoints.

## Índice de entidades

| Entidade | Arquivo | Endpoint base |
|---|---|---|
| Aluno | [entities/aluno.md](entities/aluno.md) | `/api/alunos` |
| Professor | [entities/professor.md](entities/professor.md) | `/api/professores` |
| Pessoa (base de Aluno/Professor) | [entities/pessoa.md](entities/pessoa.md) | `/api/pessoas` |
| Disciplina | [entities/disciplina.md](entities/disciplina.md) | `/api/disciplinas` |
| Turma | [entities/turma.md](entities/turma.md) | `/api/turmas` |
| Curso | [entities/curso.md](entities/curso.md) | `/api/cursos` |
| CursoDisciplina | [entities/curso-disciplina.md](entities/curso-disciplina.md) | `/api/cursos-disciplinas` |
| Matricula | [entities/matricula.md](entities/matricula.md) | `/api/matriculas` |
| HistoricoDisciplina | [entities/historico-disciplina.md](entities/historico-disciplina.md) | `/api/historicos-disciplinas` |
| Endereco | [entities/endereco.md](entities/endereco.md) | `/api/enderecos` |
| Telefone | [entities/telefone.md](entities/telefone.md) | `/api/telefones` |
| DadosBancarios | [entities/dados-bancarios.md](entities/dados-bancarios.md) | `/api/dados-bancarios` |
| Usuario | [entities/usuario.md](entities/usuario.md) | `/api/usuarios` |
| Autenticação | [entities/auth.md](entities/auth.md) | `/api/auth` |

## Base URL

Sem prefixo de versão ou context-path: as rotas são servidas diretamente em `/api/...` (ver `spring.application.name=numisis` em `application.properties`, sem `server.servlet.context-path`).

## Autenticação

- Login em `POST /api/auth/login` retorna um JWT (ver [entities/auth.md](entities/auth.md)).
- Todas as demais rotas `/api/**` exigem o header `Authorization: Bearer <token>`, exceto `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**` e `/h2-console/**`, que são públicas.
- Regra padrão: se a rota não bate com nenhum padrão específico, cai em `.requestMatchers("/api/**").authenticated()` — autenticado, mas sem role específica.

### Roles por área (`SecurityConfig`)

| Padrão de rota | Roles exigidas |
|---|---|
| `/api/usuarios/**` | `ADMIN` |
| `/api/professores/**` | `ADMIN`, `PROFESSOR` |
| `/api/cursos/**`, `/api/disciplinas/**`, `/api/turmas/**`, `/api/cursos-disciplinas/**` | `ADMIN`, `PROFESSOR` |
| `/api/matriculas/**`, `/api/historicos-disciplinas/**` | `ADMIN`, `PROFESSOR`, `ALUNO` |
| `/api/alunos/**` | Nenhuma regra específica em `SecurityConfig` (cai em "autenticado"), mas os métodos têm `@PreAuthorize` próprios — ver [entities/aluno.md](entities/aluno.md) |
| Qualquer outra `/api/**` | Autenticado (qualquer role) |

Enum `Role` (`domain.enums.Role`): `ALUNO`, `PROFESSOR`, `ADMIN`.

## Envelope de resposta — `AuthResponse<T>` em todos os endpoints

Todos os controllers da API respondem com o mesmo envelope, em **todas** as operações (`criar`, `listar`, `buscar`, `atualizar`, `remover`), incluindo os casos de erro:

```json
{
  "message": "texto descritivo",
  "dado": { }
}
```

- `message: string` — sempre presente nas respostas de sucesso e nos erros tratados (400/404/409/500 vindos do `GlobalExceptionHandler` ou dos `catch` dos controllers).
- `dado: T` — o payload em si (entidade, DTO, `PageResponse<T>`, array simples, ou até um objeto de exceção serializado em casos de erro 500 genérico — ver seção de erros abaixo). Em respostas só de erro/confirmação (ex.: `DELETE` bem-sucedido), `dado` pode estar ausente — trate como opcional no frontend.

Isso vale para **todas** as entidades, inclusive as que antes retornavam a entidade/lista crua sem envelope (`Endereco`, `Telefone`, `DadosBancarios`, `Usuario`, `CursoDisciplina`, `Matricula`, `Pessoa`, `Turma`, `Curso`). Não é mais necessário checar "com ou sem envelope" por entidade — sempre leia a resposta como `AuthResponse<T>` e acesse o payload em `dado`.

### Paginação — `PageResponse<T>`

Quando o endpoint aceita paginação (`Pageable` no controller), a listagem usa:

```json
{
  "content": [ /* T[] */ ],
  "page": 0,
  "size": 20,
  "totalElements": 42,
  "totalPages": 3
}
```

Parâmetros de query aceitos (padrão Spring `Pageable`): `page`, `size`, `sort` (ex.: `sort=nome,asc`).

**Atenção:** nem toda listagem é paginada. `Matricula`, `DadosBancarios`, `Endereco`, `Telefone`, `Usuario`, `CursoDisciplina` e `Pessoa` retornam um array simples (`T[]`) dentro de `dado` em `GET` de listagem, sem paginação — mas sempre dentro do envelope `AuthResponse`.

## Erros

- **Não encontrado — comportamento varia por controller.** Todos os endpoints de "buscar"/"remover" retornam `AuthResponse` com `message` explicando o problema, mas o status HTTP e a origem da mensagem não são uniformes:
  - `Endereco`, `Telefone`, `DadosBancarios`, `Usuario`, `CursoDisciplina`, `Matricula`, `Turma`, `Curso`, `Pessoa`, `Professor` (remover): usam `Optional`/checagem explícita antes de agir → retornam corretamente `404 Not Found` com `{ "message": "<Entidade> não encontrado(a) com id: {id}" }` (sem `dado`).
  - `Aluno`, `Professor` (buscar), `Disciplina`: o service lança `NaoEncontradoException`, mas o controller tem `catch (Exception e)` genérico que intercepta a exceção **antes** do `GlobalExceptionHandler` — o resultado real observado é **500**, com `dado` contendo o objeto de exceção serializado (não uma mensagem tratada). Trate também 500 como possível "não encontrado" nesses três.
  - `HistoricoDisciplina` (buscar): bug conhecido — o controller compara um `Optional` com `null` (nunca é `true`), então **sempre responde 200**, mesmo para id inexistente; nesse caso `dado` vem com a serialização de um `Optional` vazio em vez de um histórico real. Ver [entities/historico-disciplina.md](entities/historico-disciplina.md).
- **Regra de negócio violada** (`RegraNegocioException`): `AlunoController`, `ProfessorController` e `UsuarioController` têm `catch (RegraNegocioException e)` específico **antes** do catch genérico em `criar`/`atualizar`, retornando corretamente `400 Bad Request`, `{ "message": "..." }` (sem `dado`). Regras atuais que disparam essa exceção:
  - `Aluno`: CPF duplicado ("Já existe um aluno com esse CPF."); menor de idade sem `nomeMae`/`nomePai`.
  - `Aluno`/`Professor` (ao criar com `usuario` novo embutido) e `Usuario` (criar/atualizar direto): login duplicado ("Já existe um usuário com esse login.") — checado via `UsuarioRepository.existsByLogin` antes de salvar, para não depender da constraint do banco.
  - Nos demais controllers (sem esse catch específico), uma `RegraNegocioException` cairia no catch genérico e viraria 500.
- **Violação de integridade no banco** (constraint única violada sem ter sido pega antes pela validação de negócio — ex.: corrida entre duas requisições simultâneas): HTTP 409, `{ "message": "Já existe um registro com esses dados. Verifique os campos únicos (ex.: CPF, login)." }` — mensagem genérica (não é mais específica de CPF).
- **Erro de validação de `@Valid`** (`MethodArgumentNotValidException`): não há `@ExceptionHandler` específico no `GlobalExceptionHandler`, então cai no tratamento padrão do Spring (HTTP 400, formato `ProblemDetail`/RFC7807 — campos como `title`, `status`, `detail`, `errors`), **diferente** do formato `AuthResponse` usado no resto da API.
- Em resumo: **o envelope é sempre `AuthResponse`, mas o status HTTP para "não encontrado" não é confiável em todos os endpoints** (pode vir 404 ou 500, e em um caso sempre 200). Trate por status HTTP primeiro (`400`, `401`, `403`, `404`, `409`, `500`), leia `message` quando presente, e confira o arquivo da entidade para o comportamento específico de cada uma.

## Herança Pessoa → Aluno / Professor

`Aluno` e `Professor` (JPA `@Inheritance(strategy = InheritanceType.JOINED)`) herdam todos os campos de `Pessoa`: `id`, `usuario`, `nome`, `cpf`, `idade`, `dataCadastro`, `dataNascimento`, `email`, `endereco`, `telefones`. Ver [entities/pessoa.md](entities/pessoa.md) para a tabela completa — os arquivos de Aluno/Professor não repetem esses campos em detalhe.

## Enums

- `Role`: `ALUNO` | `PROFESSOR` | `ADMIN` (usado em `Usuario.role`)
- `StatusHistorico`: `APROVADO` | `REPROVADO` | `EM_ANDAMENTO` (usado em `HistoricoDisciplina.situacao`)

## Cuidados de segurança para o frontend

- `Usuario.senha` é retornada pela API (hash BCrypt) em qualquer endpoint que serialize um `Usuario` cru ou uma `Pessoa`/`Aluno`/`Professor` com `usuario` aninhado (ex.: corpo de `POST/PUT /api/alunos`). **Nunca exibir esse campo na UI**, mesmo que ele venha na resposta.
