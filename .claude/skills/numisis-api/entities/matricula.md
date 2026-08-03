# Matricula

Vincula um [Aluno](aluno.md) a um [Curso](curso.md).

## Endpoint Base

`/api/matriculas`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN`, `PROFESSOR` ou `ALUNO` (a mais aberta entre as áreas acadêmicas).

## Entidade Matricula

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `dataMatricula` | string (`yyyy-MM-dd`) | |
| `situacao` | string | texto livre, sem enum |
| `aluno` | — | **`@JsonIgnore` — nunca aparece na resposta JSON** |
| `curso` | — | **`@JsonIgnore` — nunca aparece na resposta JSON** |

⚠️ Assim como em `CursoDisciplina`/`Turma`, uma `Matricula` retornada pela API mostra apenas `id`, `dataMatricula` e `situacao` — **não há como saber por essa resposta a qual aluno/curso ela pertence**. Para criar, envie `{ "aluno": { "id": ... }, "curso": { "id": ... }, "dataMatricula": "...", "situacao": "..." }`.

---

## Listar

`GET /api/matriculas`

**Sem paginação, sem envelope.** Retorno: `200 OK`, array de `Matricula`.

---

## Buscar

`GET /api/matriculas/{id}`

**Sem envelope.** Retorno: `200 OK`, ou `404 Not Found` se não existir.

---

## Criar

`POST /api/matriculas`

**Sem envelope.** Retorno: `201 Created`.

---

## Atualizar

`PUT /api/matriculas/{id}`

**Sem envelope.** Retorno: `200 OK`.

---

## Excluir

`DELETE /api/matriculas/{id}`

**Sem envelope.** Retorno: `204 No Content`, ou `404 Not Found` se não existir.

## Observações

- Único endpoint de listagem da API que não é paginado nem tem parâmetros de página — retorna sempre a lista completa.
- Assim como `CursoDisciplina`, o `@JsonIgnore` em `aluno`/`curso` limita o uso direto desta resposta para exibir "matrículas de um aluno" — considere obter esse dado por outra via (ex.: relação `Aluno.matriculas`, também sujeita a lazy-loading) até que o backend exponha `alunoId`/`cursoId`.
- `GET /api/alunos` e `GET /api/alunos/{id}` já expõem os **nomes** dos cursos de cada aluno em `AlunoDTO.cursos` (derivado de `Matricula.curso.nome` no `AlunoMapper`) — para exibir "em quais cursos o aluno está matriculado" na UI, prefira esse campo em vez de cruzar dados deste endpoint manualmente. Ver [aluno.md](aluno.md).
