# HistoricoDisciplina

Registro de desempenho de um [Aluno](aluno.md) em uma [Turma](turma.md) (nota, faltas, situação).

## Endpoint Base

`/api/historicos-disciplinas`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN`, `PROFESSOR` ou `ALUNO`.

Todos os métodos usam o envelope `AuthResponse<T>`.

## Entidade HistoricoDisciplina

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `nota` | number | |
| `faltas` | number | |
| `ano` | number | |
| `semestre` | number | |
| `situacao` | `"APROVADO" \| "REPROVADO" \| "EM_ANDAMENTO"` | enum `StatusHistorico` |
| `aluno` | — | **`@JsonIgnore` — nunca aparece na resposta JSON** |
| `turma` | — | **`@JsonIgnore` — nunca aparece na resposta JSON** |

⚠️ Igual a `Matricula`/`CursoDisciplina`: para criar, envie `{ "aluno": { "id": ... }, "turma": { "id": ... }, "nota": ..., "faltas": ..., "ano": ..., "semestre": ..., "situacao": "..." }`, mas a resposta não devolve esses vínculos.

---

## Listar

`GET /api/historicos-disciplinas`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Históricos retornados com sucesso!",
  "dado": {
    "content": [ /* HistoricoDisciplina[] */ ],
    "page": 0, "size": 20, "totalElements": 7, "totalPages": 1
  }
}
```

---

## Buscar

`GET /api/historicos-disciplinas/{id}`

Retorno esperado: `200 OK`, `{ "message": "Histórico encontrado com sucesso!", "dado": { /* HistoricoDisciplina */ } }`.

⚠️ **Bug conhecido:** o controller compara o retorno do service (`Optional<HistoricoDisciplina>`) com `null` para decidir se retorna 404 — como um `Optional` nunca é `null`, essa checagem nunca é verdadeira. Na prática, **este endpoint sempre responde 200**, mesmo para um id inexistente; nesse caso `dado` vem com a representação serializada de um `Optional` vazio em vez de um objeto de histórico. No frontend, valide se `dado` tem os campos esperados (`id`, `nota`, etc.) antes de usar, não confie apenas no status HTTP aqui.

---

## Criar

`POST /api/historicos-disciplinas`

Retorno: `201 Created`, `{ "message": "Histórico salvo com sucesso!", "dado": { /* HistoricoDisciplina */ } }`.

---

## Atualizar

`PUT /api/historicos-disciplinas/{id}`

Retorno: `200 OK`, `{ "message": "Histórico atualizado com sucesso!", "dado": { /* HistoricoDisciplina */ } }`.

---

## Excluir

`DELETE /api/historicos-disciplinas/{id}`

Retorno: `204 No Content` (sem corpo — o controller usa `ResponseEntity.noContent().build()`, ignorando o envelope `AuthResponse` neste caso específico).

## Observações

- Verifique sempre o conteúdo de `dado` em `GET /{id}`, já que o status HTTP não é confiável para diferenciar "encontrado" de "não encontrado" neste endpoint.
