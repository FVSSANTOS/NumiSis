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
| `ano` | number \| `null` | **não é mais coluna própria** — campo derivado (getter `@Transient`) de `turma.ano`. `null` se o histórico não tiver `turma` vinculada, **ou se a resposta for a de um `criar`/`atualizar` recém-feito** (ver nota abaixo) |
| `semestre` | number \| `null` | idem, derivado de `turma.semestre` |
| `situacao` | `"APROVADO" \| "REPROVADO" \| "EM_ANDAMENTO"` | enum `StatusHistorico` |
| `aluno` | — | **write-only** (`@JsonProperty(access = WRITE_ONLY)`) — aceito no `POST`/`PUT`, nunca aparece na resposta |
| `turma` | — | **write-only**, mesma regra |
| `alunoId` | number \| `null` | campo derivado (getter `@Transient`, não é coluna) — id do aluno do histórico. **Vem preenchido já na resposta de `criar`/`atualizar`** (só depende do `id` que já veio no corpo) |
| `alunoNome` | string \| `null` | campo derivado — nome do aluno (`aluno.nome`). Vem `null` na resposta de `criar`/`atualizar` recém-feito (ver nota abaixo) |
| `disciplina` | string \| `null` | campo derivado (getter `@Transient`, não é coluna no banco) — nome da disciplina da turma, resolvido via `turma.getDisciplinaNome()`. `null` se o histórico não tiver `turma`, a turma não tiver `disciplina` vinculada, ou a resposta for de um `criar`/`atualizar` recém-feito (ver nota abaixo) |

`ano`/`semestre` eram redundantes com os mesmos campos em `Turma` — uma turma já representa um ano+semestre específico, então o histórico de um aluno numa turma sempre tem o mesmo ano/semestre da turma. Removi as colunas duplicadas de `historico_disciplina`; `ano`/`semestre` continuam aparecendo no JSON (mesmo nome, mesmo tipo), só que agora são sempre iguais aos da `Turma` associada, nunca dessincronizam.

Para criar, envie `{ "aluno": { "id": ... }, "turma": { "id": ... }, "nota": ..., "faltas": ..., "situacao": "..." }` — isso vincula de verdade (`aluno`/`turma` usam `@JsonProperty(WRITE_ONLY)`, testado ponta a ponta). **Não envie `ano`/`semestre`**, eles não têm mais setter e são ignorados silenciosamente se enviados.

⚠️ **`ano`, `semestre`, `disciplina` e `alunoNome` vêm `null` na resposta de `POST`/`PUT`, mesmo com o vínculo salvo corretamente.** Isso acontece porque, logo após salvar, os objetos `turma`/`aluno` em memória ainda são só o "esqueleto" que veio do corpo da requisição (`{ "id": ... }`, sem os demais campos carregados do banco) — os getters derivados navegam esses objetos e retornam `null`. `alunoId` é exceção: vem preenchido mesmo na resposta de `criar`/`atualizar`, porque só depende do `id` que já estava no esqueleto. Um `GET /api/historicos-disciplinas/{id}` (ou `listar`/`listar por aluno`/`listar por turma`) logo em seguida já traz todos os campos corretos, porque aí a entidade é recarregada do banco (com `JOIN FETCH` em `aluno`/`turma`, inclusive). Se o frontend precisa desses campos imediatamente após criar, faça um novo `GET` em vez de usar a resposta do `POST` diretamente.

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

Query params de paginação: `page`, `size`, `sort`. Filtra por `HistoricoDisciplina.aluno.id` — implementado como `@Query` explícito com `JOIN FETCH hd.aluno, hd.turma` (não é query derivada por nome de método: colidiria com o getter `@Transient getAlunoId()` — ver aviso em `SKILL.md`).

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

## Listar por turma

`GET /api/historicos-disciplinas/turma/{turmaId}`

Query params de paginação: `page`, `size`, `sort`. Filtra por `HistoricoDisciplina.turma.id` (`@Query` com `JOIN FETCH`, mesmo padrão de "Listar por aluno").

Retorno: `200 OK`

```json
{
  "message": "Históricos da turma retornados com sucesso!",
  "dado": {
    "content": [
      { "id": 1, "nota": null, "faltas": 0, "situacao": "EM_ANDAMENTO", "ano": 2026, "semestre": 1, "disciplina": "Cálculo I", "alunoId": 2, "alunoNome": "Fulano" }
    ],
    "page": 0, "size": 20, "totalElements": 1, "totalPages": 1
  }
}
```

**Este é o endpoint pra montar a tela de "alunos matriculados numa turma" (chamada de turma/diário de classe)**: cada item já traz `alunoId`/`alunoNome`, sem precisar de outro request pra resolver quem é o aluno. Se `turmaId` não existir, retorna `200 OK` com `content: []` (mesmo padrão de "Listar por aluno").

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
