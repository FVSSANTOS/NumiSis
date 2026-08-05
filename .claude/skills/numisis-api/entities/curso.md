# Curso

## Endpoint Base

`/api/cursos`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN` ou `PROFESSOR` para todas as operações.

## Entidade Curso (usada em todos os endpoints — não há DTO)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `nome` | string | |
| `descricao` | string | |
| `cursoDisciplinas` | `CursoDisciplina[]` | vínculos com disciplinas — ver [curso-disciplina.md](curso-disciplina.md). Como `CursoDisciplina.curso` e `.disciplina` são `@JsonIgnore`, cada item desta lista aparece na prática como `{ "id": number }` apenas |

---

## Listar

`GET /api/cursos`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Cursos retornados com sucesso!",
  "dado": {
    "content": [ /* Curso[] */ ],
    "page": 0, "size": 20, "totalElements": 2, "totalPages": 1
  }
}
```

---

## Buscar

`GET /api/cursos/{id}`

Retorno: `200 OK`, `{ "message": "Curso encontrado com sucesso!", "dado": { /* Curso */ } }`, ou `404 Not Found`, `{ "message": "Curso não encontrado com id: {id}" }` se não existir.

---

## Criar

`POST /api/cursos`

Body: `{ "nome": string, "descricao": string }`.

Retorno: `201 Created`, `{ "message": "Curso salvo com sucesso!", "dado": { /* Curso */ } }`.

---

## Atualizar

`PUT /api/cursos/{id}`

Retorno: `200 OK`, `{ "message": "Curso atualizado com sucesso!", "dado": { /* Curso */ } }`.

---

## Excluir

`DELETE /api/cursos/{id}`

Retorno: `204 No Content`, `{ "message": "Curso deletado com sucesso!" }`, ou `404 Not Found`, `{ "message": "Curso não encontrado com id: {id}" }` se não existir.

## Observações

- `buscar`/`remover` propagam corretamente o 404 (usam `Optional`/checagem antes de agir).
