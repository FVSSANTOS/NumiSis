# Matricula

Vincula um [Aluno](aluno.md) a um [Curso](curso.md).

## Endpoint Base

`/api/matriculas`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN`, `PROFESSOR` ou `ALUNO` (a mais aberta entre as áreas acadêmicas).

## Entidade Matricula

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `dataMatricula` | string (`yyyy-MM-dd`) | |
| `situacao` | string | texto livre, sem enum |
| `aluno` | — | **write-only** (`@JsonProperty(access = WRITE_ONLY)`) — aceito no `POST`/`PUT`, nunca aparece na resposta |
| `curso` | — | **write-only**, mesma regra |
| `alunoId` | number \| `null` | campo derivado (getter `@Transient`, não é coluna) — id do aluno da matrícula |
| `alunoNome` | string \| `null` | campo derivado — nome do aluno da matrícula (`aluno.nome`). **Vem `null` na resposta de `criar`/`atualizar`** — ver nota abaixo |
| `cursoId` | number \| `null` | campo derivado — id do curso da matrícula |
| `cursoNome` | string \| `null` | campo derivado — nome do curso da matrícula (`curso.nome`). Mesma ressalva de `alunoNome` |

Para criar, envie `{ "aluno": { "id": ... }, "curso": { "id": ... }, "dataMatricula": "...", "situacao": "..." }` — isso vincula de verdade (`aluno`/`curso` usam `@JsonProperty(WRITE_ONLY)`, testado ponta a ponta).

⚠️ **`alunoNome`/`cursoNome` vêm `null` na resposta de `POST`/`PUT`, mesmo com o vínculo salvo corretamente** (mesmo comportamento de `Turma.disciplinaNome`/`.professorNome` — ver `SKILL.md`). Logo após salvar, os objetos `aluno`/`curso` em memória ainda são só o "esqueleto" enviado no corpo (`{ "id": ... }`), sem `nome` carregado do banco — os getters derivados retornam `null` nesse momento. `alunoId`/`cursoId`, por outro lado, **vêm preenchidos já na resposta de `criar`/`atualizar`** (só dependem do `id` que já estava no esqueleto, não precisam recarregar nada). Um `GET /api/matriculas` ou `GET /api/matriculas/{id}` logo em seguida já traz `alunoNome`/`cursoNome` corretos, porque aí a entidade é recarregada do banco.

---

## Listar

`GET /api/matriculas`

**Sem paginação.** Retorno: `200 OK`

```json
{
  "message": "Matrículas retornadas com sucesso!",
  "dado": [
    { "id": 1, "dataMatricula": "2026-01-01", "situacao": "ATIVA", "alunoId": 1, "alunoNome": "Fulano", "cursoId": 3, "cursoNome": "Engenharia" }
  ]
}
```

---

## Buscar

`GET /api/matriculas/{id}`

Retorno: `200 OK`, `{ "message": "Matrícula encontrada com sucesso!", "dado": { /* Matricula */ } }`, ou `404 Not Found`, `{ "message": "Matrícula não encontrada com id: {id}" }` (sem `dado`) se não existir.

---

## Criar

`POST /api/matriculas`

Retorno: `201 Created`, `{ "message": "Matrícula salva com sucesso!", "dado": { /* Matricula */ } }`.

---

## Atualizar

`PUT /api/matriculas/{id}`

Retorno: `200 OK`, `{ "message": "Matrícula atualizada com sucesso!", "dado": { /* Matricula */ } }`.

---

## Excluir

`DELETE /api/matriculas/{id}`

Retorno: `204 No Content`, `{ "message": "Matrícula deletada com sucesso!" }`, ou `404 Not Found`, `{ "message": "Matrícula não encontrada com id: {id}" }` se não existir.

## Observações

- Único endpoint de listagem da API que não é paginado nem tem parâmetros de página — retorna sempre a lista completa. Para uma tela de matrícula (ex.: multi-select de alunos de um curso, ou lista de cursos de um aluno com id/situação/data pra editar/excluir), filtre `GET /api/matriculas` no frontend por `alunoId`/`cursoId` — não existe hoje um `GET /api/matriculas/aluno/{alunoId}` ou `/curso/{cursoId}` dedicado (diferente de `HistoricoDisciplina`, que tem `/aluno/{alunoId}`). Peça se precisar.
- `GET /api/alunos` e `GET /api/alunos/{id}` já expõem os **nomes** dos cursos de cada aluno em `AlunoDTO.cursos` (derivado de `Matricula.curso.nome` no `AlunoMapper`) — mas só o nome, sem o `id` da matrícula (não dá pra editar/excluir a partir dali). Para isso, use os campos derivados desta entidade (`alunoId`/`cursoId`/`alunoNome`/`cursoNome`) descritos acima.
