# Turma

## Endpoint Base

`/api/turmas`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN` ou `PROFESSOR` para todas as operações.

## Entidade Turma (usada em todos os endpoints — não há DTO em uso)

> Existem `TurmaDTO` e `TurmaMapper` no projeto, mas **nenhum método de `TurmaController` os utiliza** — todos retornam a entidade crua dentro do envelope `AuthResponse`.

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `ano` | number | |
| `semestre` | number | |
| `sala` | string | |
| `horarioInicio` | string | |
| `horarioTermino` | string | |
| `disciplina` | — | **write-only** (`@JsonProperty(access = WRITE_ONLY)`) — aceito no `POST`/`PUT` (`{ "disciplina": { "id": ... } }`), nunca aparece na resposta |
| `disciplinaId` | number \| `null` | campo derivado (getter `@Transient`, não é coluna) — id da disciplina da turma |
| `disciplinaNome` | string \| `null` | campo derivado — nome da disciplina da turma (`disciplina.nome`). **Vem `null` na resposta de `criar`/`atualizar`** — ver nota abaixo |
| `professor` | — | **write-only**, mesma regra — aceito como `{ "professor": { "id": ... } }` |
| `professorId` | number \| `null` | campo derivado — id do professor da turma |
| `professorNome` | string \| `null` | campo derivado — nome do professor da turma (`professor.nome`). Mesma ressalva de `disciplinaNome` |
| `historicos` | `HistoricoDisciplina[]` | históricos vinculados a esta turma, **lista completa sem paginação** (relação `@OneToMany` direta, sem `@JsonIgnore`). Para uma tela de turma (diário de classe/lista de alunos), prefira `GET /api/historicos-disciplinas/turma/{turmaId}` — mesmo dado, mas paginado e cada item já vem com `alunoId`/`alunoNome` resolvidos (ver [historico-disciplina.md](historico-disciplina.md)) |

Envie `{ "disciplina": { "id": ... }, "professor": { "id": ... } }` no `POST`/`PUT` para vincular — funciona de verdade (testado ponta a ponta): `disciplina`/`professor` usam `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)`, não `@JsonIgnore` (ver `SKILL.md` para a diferença).

⚠️ **`disciplinaNome`/`professorNome` vêm `null` na resposta de `POST`/`PUT`, mesmo com o vínculo salvo corretamente.** Logo após salvar, os objetos `disciplina`/`professor` em memória ainda são só o "esqueleto" enviado no corpo (`{ "id": ... }`, sem `nome` carregado do banco) — os getters derivados retornam `null` nesse momento. `disciplinaId`/`professorId`, por outro lado, **vêm preenchidos já na resposta de `criar`/`atualizar`** (só dependem do `id` que já estava no esqueleto). Um `GET /api/turmas/{id}` (ou `listar`) logo em seguida já traz `disciplinaNome`/`professorNome` corretos, porque aí a entidade é recarregada do banco. **Se a tela de edição de turma precisa mostrar o professor/disciplina atual pra evitar trocar sem querer, use os dados de um `GET` (buscar/listar), não a resposta de um `POST`/`PUT` anterior.**

---

## Listar

`GET /api/turmas`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Turmas retornadas com sucesso!",
  "dado": {
    "content": [ /* Turma[] (com disciplinaId/disciplinaNome/professorId/professorNome, mas sem disciplina/professor) */ ],
    "page": 0, "size": 20, "totalElements": 4, "totalPages": 1
  }
}
```

---

## Buscar

`GET /api/turmas/{id}`

Retorno: `200 OK`, `{ "message": "Turma encontrada com sucesso!", "dado": { /* Turma */ } }`, ou `404 Not Found`, `{ "message": "Turma não encontrada com id: {id}" }` se não existir.

---

## Criar

`POST /api/turmas`

Retorno: `201 Created`, `{ "message": "Turma salva com sucesso!", "dado": { /* Turma */ } }`.

---

## Atualizar

`PUT /api/turmas/{id}`

Retorno: `200 OK`, `{ "message": "Turma atualizada com sucesso!", "dado": { /* Turma */ } }`.

---

## Excluir

`DELETE /api/turmas/{id}`

Retorno: `204 No Content`, `{ "message": "Turma deletada com sucesso!" }`, ou `404 Not Found`, `{ "message": "Turma não encontrada com id: {id}" }` se o id não existir.

## Observações

- `buscar`/`remover` propagam corretamente o 404 quando o id não existe (usam `Optional`/checagem antes de agir, sem `catch` genérico mascarando o erro).
