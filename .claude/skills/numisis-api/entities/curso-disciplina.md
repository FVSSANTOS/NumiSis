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

**Sem paginação, sem envelope.** Retorno: `200 OK`, array de `CursoDisciplina` (cada item praticamente só com `id`).

---

## Buscar

`GET /api/cursos-disciplinas/{id}`

**Sem envelope.** Retorno: `200 OK`, ou `404 Not Found` se não existir.

---

## Criar

`POST /api/cursos-disciplinas`

**Sem envelope.** Retorno: `201 Created`.

---

## Atualizar

`PUT /api/cursos-disciplinas/{id}`

**Sem envelope.** Retorno: `200 OK`.

---

## Excluir

`DELETE /api/cursos-disciplinas/{id}`

**Sem envelope.** Retorno: `204 No Content`, ou `404 Not Found` se não existir.

## Observações

- Nenhum método usa `AuthResponse` neste controller (diferente de Curso/Disciplina, que envelopam ao menos a listagem).
- Dado o `@JsonIgnore` em ambas as relações, esta entidade tem utilidade limitada para exibição direta na UI — sirva-se dela apenas para criar/remover vínculos, não para exibir "quais disciplinas um curso tem" (prefira montar essa visão a partir de outra fonte, ou solicite ajuste no backend para expor `cursoId`/`disciplinaId`).
