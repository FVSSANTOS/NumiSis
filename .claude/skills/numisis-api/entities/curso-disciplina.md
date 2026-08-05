# CursoDisciplina

Entidade de associação entre [Curso](curso.md) e [Disciplina](disciplina.md).

## Endpoint Base

`/api/cursos-disciplinas`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN` ou `PROFESSOR` para todas as operações.

## Entidade CursoDisciplina

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `curso` | — | **`@JsonIgnore` — nunca aparece na resposta JSON** |
| `disciplina` | — | **`@JsonIgnore` — nunca aparece na resposta JSON** |

⚠️ **Importante:** como ambas as relações são `@JsonIgnore`, o `GET` de um `CursoDisciplina` retorna efetivamente **apenas `{ "id": number }`** — não é possível descobrir por essa resposta a qual curso/disciplina o vínculo se refere. Para vincular curso e disciplina, envie `{ "curso": { "id": ... }, "disciplina": { "id": ... } }` no `POST`/`PUT` (o `@JsonIgnore` do Jackson também bloqueia a desserialização por padrão — confirme com o backend que esse vínculo está realmente sendo salvo antes de depender disso na tela).

---

## Listar

`GET /api/cursos-disciplinas`

**Sem paginação.** Retorno: `200 OK`

```json
{ "message": "Vínculos entre cursos e disciplinas retornados com sucesso!", "dado": [ /* CursoDisciplina[] (cada item praticamente só com id) */ ] }
```

---

## Buscar

`GET /api/cursos-disciplinas/{id}`

Retorno: `200 OK`, `{ "message": "Vínculo entre curso e disciplina encontrado com sucesso!", "dado": { /* CursoDisciplina */ } }`, ou `404 Not Found`, `{ "message": "Vínculo entre curso e disciplina não encontrado com id: {id}" }` se não existir.

---

## Criar

`POST /api/cursos-disciplinas`

Retorno: `201 Created`, `{ "message": "Vínculo entre curso e disciplina salvo com sucesso!", "dado": { /* CursoDisciplina */ } }`.

---

## Atualizar

`PUT /api/cursos-disciplinas/{id}`

Retorno: `200 OK`, `{ "message": "Vínculo entre curso e disciplina atualizado com sucesso!", "dado": { /* CursoDisciplina */ } }`.

---

## Excluir

`DELETE /api/cursos-disciplinas/{id}`

Retorno: `204 No Content`, `{ "message": "Vínculo entre curso e disciplina deletado com sucesso!" }`, ou `404 Not Found`, `{ "message": "Vínculo entre curso e disciplina não encontrado com id: {id}" }` se não existir.

## Observações

- Dado o `@JsonIgnore` em ambas as relações, esta entidade tem utilidade limitada para exibição direta na UI — sirva-se dela apenas para criar/remover vínculos, não para exibir "quais disciplinas um curso tem" (prefira montar essa visão a partir de outra fonte, ou solicite ajuste no backend para expor `cursoId`/`disciplinaId`).
