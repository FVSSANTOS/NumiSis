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

Não existe conceito de "grade curricular" (quais disciplinas pertencem a um curso) na API — foi removido de propósito (havia uma entidade `CursoDisciplina`/endpoint `/api/cursos-disciplinas`, descontinuados). Um `Curso` é só `id`/`nome`/`descricao`; ver "Observações" abaixo para como aluno, curso e disciplina se relacionam sem esse conceito.

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
- **Como aluno, curso e disciplina se relacionam** (sem grade curricular): `Matricula` liga o aluno ao curso (`Aluno --[Matricula]--> Curso`, ver [matricula.md](matricula.md)) — isso é só a inscrição do aluno no programa. Já quais disciplinas o aluno está cursando é outra coisa, completamente independente do curso: `Aluno --[HistoricoDisciplina]--> Turma --> Disciplina` (ver [historico-disciplina.md](historico-disciplina.md) e [turma.md](turma.md)). Uma `Turma` pode ser criada para qualquer `Disciplina`, sem precisar de nenhum vínculo prévio com o `Curso` do aluno — e um aluno pode ter múltiplos `HistoricoDisciplina` para a mesma `Disciplina` (via `Turma`s diferentes, em anos/semestres diferentes), o que cobre naturalmente o caso de repetência/reoferta.
