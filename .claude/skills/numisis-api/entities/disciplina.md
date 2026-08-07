# Disciplina

## Endpoint Base

`/api/disciplinas`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN` ou `PROFESSOR` para todas as operações.

## DisciplinaDTO

Usado em `listar` e `buscar`.

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `nome` | string | |
| `descricao` | string | |

## Entidade Disciplina (usada em `criar`/`atualizar`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `nome` | string | |
| `descricao` | string | |
| `turmas` | `Turma[]` | turmas desta disciplina |

Não há vínculo direto entre `Disciplina` e `Curso` — esse conceito de "grade curricular" foi removido de propósito. Uma disciplina só se conecta a um curso indiretamente, através de alunos matriculados no curso que cursam turmas dessa disciplina. Ver [curso.md](curso.md) (seção "Observações") para a explicação completa.

---

## Listar

`GET /api/disciplinas`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Disciplinas retornadas com sucesso!",
  "dado": {
    "content": [ /* DisciplinaDTO[] */ ],
    "page": 0, "size": 20, "totalElements": 3, "totalPages": 1
  }
}
```

---

## Buscar

`GET /api/disciplinas/{id}`

Retorno: `200 OK`

```json
{ "message": "Disciplina encontrada com sucesso", "dado": { /* DisciplinaDTO */ } }
```

---

## Criar

`POST /api/disciplinas`

Body: `{ "nome": string, "descricao": string }` (sem validações declaradas).

Retorno: `201 Created`, `{ "message": "Disciplina salva com sucesso!", "dado": { /* Disciplina crua, inclui turmas: [] vazio */ } }`.

---

## Atualizar

`PUT /api/disciplinas/{id}`

Body: mesmo formato de Criar.

Retorno: `200 OK`

```json
{ "message": "Disciplina atualizada com sucesso!", "dado": { /* Disciplina crua */ } }
```

---

## Excluir

`DELETE /api/disciplinas/{id}`

Retorno: `204 No Content`, `{ "message": "Disciplina deletada com sucesso!" }`.

Id inexistente: o controller checa `if (disciplinaService.buscarPorId(id) == null)`, mas `buscarPorId` lança `NaoEncontradoException` em vez de retornar `null` — essa checagem nunca dispara. A exceção acaba caindo no `catch (Exception e)` genérico, então o resultado real observado é **500** (`{ "message": "Erro no processamento do servidor", "dado": { /* exceção serializada */ } }`), não 404.

## Observações

- `listar`/`buscar` retornam `DisciplinaDTO` (enxuto); `criar`/`atualizar` retornam a entidade completa. Não assuma o mesmo shape entre operações.
