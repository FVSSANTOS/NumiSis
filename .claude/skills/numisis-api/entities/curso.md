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

**Sem envelope.** Retorno: `200 OK` com o `Curso` cru, ou `404 Not Found` se não existir.

---

## Criar

`POST /api/cursos`

Body: `{ "nome": string, "descricao": string }`.

**Sem envelope.** Retorno: `201 Created` com o `Curso` criado.

---

## Atualizar

`PUT /api/cursos/{id}`

**Sem envelope.** Retorno: `200 OK` com o `Curso` atualizado.

---

## Excluir

`DELETE /api/cursos/{id}`

**Sem envelope.** Retorno: `204 No Content`, ou `404 Not Found` se não existir.

## Observações

- Único método deste controller com envelope `AuthResponse` é `listar`; os demais retornam a entidade crua.
- `buscar`/`remover` propagam corretamente o 404 (usam `Optional`/checagem antes de agir).
