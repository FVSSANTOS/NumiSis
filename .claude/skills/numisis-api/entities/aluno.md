# Aluno

Estende [Pessoa](pessoa.md) (campos `id`, `nome`, `cpf`, `idade`, `dataCadastro`, `dataNascimento`, `email`, `endereco`, `telefones`, `usuario`).

## Endpoint Base

`/api/alunos`

## Autenticação e Permissões

Todas as rotas exigem JWT. Permissões definidas via `@PreAuthorize` no controller (mais restritivas do que a regra genérica de `SecurityConfig`):

| Ação | Roles permitidas |
|---|---|
| Criar | `ADMIN` |
| Listar | `ADMIN`, `PROFESSOR` |
| Buscar por id | `ADMIN`, `PROFESSOR` |
| Atualizar | `ADMIN` |
| Excluir | qualquer usuário autenticado (sem `@PreAuthorize`) |

Todas as respostas usam o envelope `AuthResponse<T>` (ver `SKILL.md`).

---

## Listar

`GET /api/alunos`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Alunos retornados com sucesso!",
  "dado": {
    "content": [ /* AlunoDTO[] */ ],
    "page": 0,
    "size": 20,
    "totalElements": 10,
    "totalPages": 1
  }
}
```

Os itens de `content` são `AlunoDTO` (não a entidade completa) — ver tabela abaixo.

---

## Buscar

`GET /api/alunos/{id}`

Retorno: `200 OK`

```json
{ "message": "Aluno encontrado com sucesso", "dado": { /* AlunoDTO */ } }
```

### AlunoDTO

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `nome` | string | |
| `cpf` | string | |
| `idade` | number | |
| `dataNascimento` | string (`yyyy-MM-dd`) | |
| `nomePai` | string \| null | |
| `nomeMae` | string \| null | |
| `telefones` | `Telefone[]` | ver [telefone.md](telefone.md) |
| `email` | string | |
| `condicaoEspecial` | string | |
| `alergia` | string | |
| `cursos` | `string[]` | nomes (distintos) dos cursos em que o aluno tem matrícula, derivados de `Aluno.matriculas[].curso.nome`. Lista vazia (`[]`) se o aluno não estiver matriculado em nenhum curso. Não traz `id` do curso, apenas o nome — se a tela precisar do id para navegação, será necessário ajuste adicional no backend (`AlunoMapper`/`AlunoDTO`) |

Note que `AlunoDTO` **não** inclui `endereco`, `usuario`, `dataCadastro`, `matriculas` (a lista bruta) nem `historicos`, mesmo esses existindo na entidade — só aparecem no corpo de `criar`/`atualizar` (ver abaixo), que retornam a entidade crua.

---

## Criar

`POST /api/alunos`

Body: entidade `Aluno` completa (não o DTO).

| Campo | Tipo | Obrigatório | Observações |
|---|---|---|---|
| `nome` | string | não (sem validação declarada) | |
| `cpf` | string | não (sem `@NotBlank`, mas validado por `@CPF` se enviado) | único no banco |
| `idade` | number | não | usado para regra de menor de idade abaixo |
| `dataNascimento` | string (`yyyy-MM-dd`) | não | |
| `dataCadastro` | string (ISO datetime) | não | |
| `email` | string | não | |
| `endereco` | objeto `Endereco` | não | persistido em cascata se enviado |
| `usuario` | objeto `Usuario` (`login`, `senha`, `role`) | não | persistido em cascata se enviado; senha **não** é criptografada automaticamente aqui (diferente de `/api/usuarios`) |
| `nomeMae` | string | condicional | obrigatório se `idade < 18` |
| `nomePai` | string | condicional | obrigatório se `idade < 18` |
| `condicaoEspecial` | string | sim (`@NotBlank`) | |
| `alergia` | string | sim (`@NotBlank`) | |

Regras de negócio (`AlunoService.salvar`):
- CPF já cadastrado → `RegraNegocioException` ("Já existe um aluno com esse CPF."), mas como `AlunoController` tem `catch (Exception e)` genérico, o resultado real observado é **500**, com `dado` contendo a exceção — não 400.
- `idade < 18` sem `nomeMae` ou `nomePai` → mesma situação (mensagem "Para menores de idade, nome do pai e da mãe são obrigatórios.", mas retornado como 500 pelo motivo acima).

Retorno esperado (sucesso): `201 Created`

```json
{ "message": "Aluno salvo com sucesso!", "dado": { /* Aluno completo, entidade crua */ } }
```

---

## Atualizar

`PUT /api/alunos/{id}`

Body: mesmo formato de Criar (entidade `Aluno` completa). O `id` da URL não é copiado automaticamente para o objeto antes de salvar — envie o `id` também no corpo se o backend depender dele (`AlunoService.atualizar` chama `buscarPorId(aluno.getId())`, usando o id do corpo, não o da URL).

Retorno: `200 OK`, `AuthResponse<Aluno>` (entidade crua).

---

## Excluir

`DELETE /api/alunos/{id}`

Retorno: `204 No Content`, `AuthResponse` só com `message` (sem `dado`).

## Observações

- Erros de "não encontrado" (`GET`/`PUT`/`DELETE` com id inexistente) resultam em **500**, não 404, porque o controller intercepta todas as exceções genericamente. Trate `500` como possível "não encontrado" neste endpoint.
- Listagem/Busca retornam `AlunoDTO` (enxuto); Criação/Atualização retornam a entidade `Aluno` completa (com `endereco`, `usuario`, `telefones`, `matriculas`, `historicos`). Não assuma o mesmo shape entre as operações.
