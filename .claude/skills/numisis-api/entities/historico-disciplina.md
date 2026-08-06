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
| `disciplina` | string \| `null` | campo derivado (getter `@Transient`, não é coluna no banco) — nome da disciplina da turma, resolvido via `turma.disciplina.nome`. `null` se o histórico não tiver `turma` ou a turma não tiver `disciplina` vinculada. Existe justamente para contornar os dois `@JsonIgnore` acima (`HistoricoDisciplina.turma` e `Turma.disciplina`), que impediam chegar no nome da disciplina a partir do histórico |

⚠️ Igual a `Matricula`/`CursoDisciplina`: para criar, envie `{ "aluno": { "id": ... }, "turma": { "id": ... }, "nota": ..., "faltas": ..., "ano": ..., "semestre": ..., "situacao": "..." }`, mas a resposta não devolve esses vínculos (exceto o nome da disciplina, via `disciplina`, conforme acima).

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

## Listar por aluno

`GET /api/historicos-disciplinas/aluno/{alunoId}`

Query params de paginação: `page`, `size`, `sort`. Filtra por `HistoricoDisciplina.aluno.id` (`findByAlunoId`).

Retorno: `200 OK`

```json
{
  "message": "Históricos do aluno retornados com sucesso!",
  "dado": {
    "content": [ /* HistoricoDisciplina[] (apenas os do aluno informado) */ ],
    "page": 0, "size": 20, "totalElements": 3, "totalPages": 1
  }
}
```

Se `alunoId` não existir ou o aluno não tiver históricos, retorna `200 OK` com `content: []` (não há checagem de existência do aluno — mesmo padrão dos demais endpoints de listagem da API, que não validam ids usados como filtro).

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

Retorno: `204 No Content`, `{ "message": "Histórico deletado com sucesso!" }`.

## Observações

- Verifique sempre o conteúdo de `dado` em `GET /{id}`, já que o status HTTP não é confiável para diferenciar "encontrado" de "não encontrado" neste endpoint.
