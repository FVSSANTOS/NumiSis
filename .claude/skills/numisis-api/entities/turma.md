# Turma

## Endpoint Base

`/api/turmas`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN` ou `PROFESSOR` para todas as operações.

## Entidade Turma (usada em todos os endpoints — não há DTO em uso)

> Existem `TurmaDTO` e `TurmaMapper` no projeto, mas **nenhum método de `TurmaController` os utiliza** — todos retornam a entidade crua.

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `ano` | number | |
| `semestre` | number | |
| `sala` | string | |
| `horarioInicio` | string | |
| `horarioTermino` | string | |
| `disciplina` | — | **`@JsonIgnore` — nunca aparece na resposta JSON**, mesmo estando presente na entidade |
| `professor` | — | **`@JsonIgnore` — nunca aparece na resposta JSON** |
| `historicos` | `HistoricoDisciplina[]` | históricos vinculados a esta turma |

⚠️ **Importante para o frontend:** como `disciplina` e `professor` são `@JsonIgnore`, uma `Turma` retornada por qualquer endpoint deste controller **não informa a qual disciplina ou professor pertence**. Não há campo `disciplinaId`/`professorId` disponível na resposta (o `TurmaDTO` que teria esses campos não é usado). Se a tela precisar exibir disciplina/professor de uma turma, será necessário buscar essa relação por outro caminho (ex.: listar turmas a partir de `GET /api/disciplinas/{id}` — que também não expõe isso hoje — ou solicitar ajuste no backend).

Ao **enviar** (`POST`/`PUT`) uma Turma, `disciplina` e `professor` ainda podem ser preenchidos no corpo da requisição (o `@JsonIgnore` do Jackson, por padrão, também ignora na desserialização, então o valor enviado é **ignorado ao salvar** — confirme no backend antes de depender disso; o vínculo provavelmente precisa ser feito por outro meio, como `curso-disciplina`, ou o `@JsonIgnore` precisa ser trocado por `@JsonIgnoreProperties` no backend).

---

## Listar

`GET /api/turmas`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Turmas retornadas com sucesso!",
  "dado": {
    "content": [ /* Turma[] (sem disciplina/professor) */ ],
    "page": 0, "size": 20, "totalElements": 4, "totalPages": 1
  }
}
```

---

## Buscar

`GET /api/turmas/{id}`

**Sem envelope.** Retorno: `200 OK` com a `Turma` crua, ou `404 Not Found` (corpo vazio) se não existir.

---

## Criar

`POST /api/turmas`

**Sem envelope.** Retorno: `201 Created` com a `Turma` criada.

---

## Atualizar

`PUT /api/turmas/{id}`

**Sem envelope.** Retorno: `200 OK` com a `Turma` atualizada.

---

## Excluir

`DELETE /api/turmas/{id}`

**Sem envelope.** Retorno: `204 No Content`, ou `404 Not Found` se o id não existir.

## Observações

- Único método deste controller com envelope `AuthResponse` é `listar`; os demais retornam a entidade crua diretamente.
- `buscar`/`remover` propagam corretamente o 404 quando o id não existe (usam `Optional`/checagem antes de agir, sem `catch` genérico mascarando o erro).
