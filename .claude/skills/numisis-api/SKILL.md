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

## Envelope de resposta — atenção, **não é uniforme**

O backend tem duas famílias de resposta convivendo, dependendo do controller/endpoint. **Sempre confira o arquivo da entidade** para saber qual se aplica antes de tipar a resposta no frontend.

### 1. Envelope `AuthResponse<T>`

Usado por `AlunoController` (todos os métodos), `ProfessorController` (criar/listar/buscar), `DisciplinaController` (listar/buscar/atualizar/remover), `TurmaController` (somente listar), `CursoController` (somente listar), `HistoricoDisciplinaController` (todos os métodos).

```json
{
  "message": "texto descritivo (pode estar ausente)",
  "dado": { }
}
```

- `message: string`
- `dado: T` — o payload em si (entidade, DTO, `PageResponse<T>`, ou até um objeto de exceção serializado em casos de erro 500 genérico — ver seção de erros abaixo).

### 2. Resposta "crua" (sem envelope)

Usado por `MatriculaController`, `DadosBancariosController`, `EnderecoController`, `TelefoneController`, `UsuarioController`, `CursoDisciplinaController`, `PessoaController`, e por parte dos métodos de `ProfessorController` (atualizar/remover), `DisciplinaController` (criar), `TurmaController` (criar/buscar/atualizar/remover) e `CursoController` (criar/buscar/atualizar/remover).

Nesses casos o corpo da resposta é a própria entidade (ou lista de entidades), sem `message`/`dado`.

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

**Atenção:** nem toda listagem é paginada. `Matricula`, `DadosBancarios`, `Endereco`, `Telefone`, `Usuario`, `CursoDisciplina` e `Pessoa` retornam um array simples (`T[]`) em `GET` de listagem, sem paginação nem envelope.

## Erros

- **Não encontrado explícito** (`NaoEncontradoException`, HTTP 404): só chega ao `GlobalExceptionHandler` quando o controller **não** engole a exceção antes. Vários controllers (`AlunoController`, `ProfessorController`, `DisciplinaController`, `HistoricoDisciplinaController`) têm `catch (Exception e)` genérico que intercepta a exceção antes do handler global — nesses casos o resultado real é **500**, com `dado` contendo o objeto de exceção serializado (não a mensagem tratada). Isso é uma inconsistência do código atual, não um comportamento a ser assumido como estável; trate tanto 404 quanto 500 como "registro não encontrado / erro" no frontend.
- **Regra de negócio violada** (`RegraNegocioException`): HTTP 400, corpo `{ "message": "..." }` (sem `dado`) — só ocorre quando a exceção escapa do controller (ex.: `AlunoController` não tem catch específico para essa exceção, então cai no genérico e vira 500 com o mesmo formato `AuthResponse`, mas com `dado` = exceção).
- **Violação de integridade no banco** (ex.: CPF duplicado detectado via constraint): HTTP 409, `{ "message": "CPF já cadastrado." }`.
- **Erro de validação de `@Valid`** (`MethodArgumentNotValidException`): não há `@ExceptionHandler` específico no `GlobalExceptionHandler`, então cai no tratamento padrão do Spring (HTTP 400, formato `ProblemDetail`/RFC7807 — campos como `title`, `status`, `detail`, `errors`), **diferente** do formato `AuthResponse` usado no resto da API.
- Em resumo: **não existe um formato de erro único e confiável**. Trate por status HTTP primeiro (`400`, `401`, `403`, `404`, `409`, `500`) e leia `message`/`detail` quando presente, com fallback genérico.

## Herança Pessoa → Aluno / Professor

`Aluno` e `Professor` (JPA `@Inheritance(strategy = InheritanceType.JOINED)`) herdam todos os campos de `Pessoa`: `id`, `usuario`, `nome`, `cpf`, `idade`, `dataCadastro`, `dataNascimento`, `email`, `endereco`, `telefones`. Ver [entities/pessoa.md](entities/pessoa.md) para a tabela completa — os arquivos de Aluno/Professor não repetem esses campos em detalhe.

## Enums

- `Role`: `ALUNO` | `PROFESSOR` | `ADMIN` (usado em `Usuario.role`)
- `StatusHistorico`: `APROVADO` | `REPROVADO` | `EM_ANDAMENTO` (usado em `HistoricoDisciplina.situacao`)

## Cuidados de segurança para o frontend

- `Usuario.senha` é retornada pela API (hash BCrypt) em qualquer endpoint que serialize um `Usuario` cru ou uma `Pessoa`/`Aluno`/`Professor` com `usuario` aninhado (ex.: corpo de `POST/PUT /api/alunos`). **Nunca exibir esse campo na UI**, mesmo que ele venha na resposta.
