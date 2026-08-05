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
| `endereco` | objeto `Endereco` \| `null` | ver [endereco.md](endereco.md); vem direto de `Aluno.getEndereco()` |
| `usuarioLogin` | string \| `null` | apenas o `login` do `Usuario` vinculado (`Aluno.usuario.login`) — a senha (hash) **não** é exposta aqui, ao contrário do corpo de `criar`/`atualizar`, que retorna o objeto `Usuario` completo |

Note que `AlunoDTO` **não** inclui `dataCadastro`, `matriculas` (a lista bruta) nem `historicos`, mesmo esses existindo na entidade — só aparecem no corpo de `criar`/`atualizar` (ver abaixo), que retornam a entidade crua.

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
| `usuario` | objeto `Usuario` (`login`, `senha` em texto puro, `role`) | não | persistido em cascata se enviado. `senha` é sempre tratada por `AlunoService.tratarSenhaUsuario` antes de salvar — ver regra de senha abaixo. Um mesmo `Usuario` não pode ficar vinculado a mais de uma pessoa (`usuario_id` é `unique` na tabela `pessoa`, validado também em código) |
| `nomeMae` | string | condicional | obrigatório se `idade < 18` |
| `nomePai` | string | condicional | obrigatório se `idade < 18` |
| `condicaoEspecial` | string | sim (`@NotBlank`) | |
| `alergia` | string | sim (`@NotBlank`) | |

Regras de negócio (`AlunoService.salvar`), todas retornando corretamente `400 Bad Request`, `{ "message": "..." }` (o controller tem `catch (RegraNegocioException e)` específico antes do catch genérico):
- CPF já cadastrado → "Já existe um aluno com esse CPF."
- `usuario` embutido, novo (`usuario.id` nulo) com `login` que já existe em outro usuário → "Já existe um usuário com esse login." (checado via `UsuarioRepository.existsByLogin`)
- `usuario` embutido referenciando um usuário **já existente** (`usuario.id` preenchido) que já está vinculado a outra pessoa → "Esse usuário já está vinculado a outra pessoa." (checado via `PessoaRepository.existsByUsuarioId`)
- `idade < 18` sem `nomeMae` ou `nomePai` → "Para menores de idade, nome do pai e da mãe são obrigatórios."

### Regra de senha do `usuario` embutido (`tratarSenhaUsuario`, usado em `criar` e `atualizar`)

| Situação | Comportamento |
|---|---|
| `usuario.id` nulo (usuário novo) | `senha` enviada (texto puro) é criptografada com BCrypt |
| `usuario.id` preenchido (usuário existente) **com** `senha` no corpo | `senha` enviada (texto puro) é criptografada com BCrypt — é assim que se troca a senha de um aluno já existente: reenvie o `usuario` com o mesmo `id`, `login`/`role` e a nova senha em texto puro |
| `usuario.id` preenchido **sem** `senha` no corpo (campo ausente/vazio) | o hash atual é mantido (busca o `Usuario` no banco e reaproveita a senha) — evita zerar a senha ao editar outros campos do aluno sem tocar no usuário |

Nunca envie um hash já criptografado no campo `senha` — em todos os casos acima o valor enviado (quando presente) é tratado como texto puro e recriptografado.

Retorno esperado (sucesso): `201 Created`

```json
{ "message": "Aluno salvo com sucesso!", "dado": { /* Aluno completo, entidade crua */ } }
```

---

## Atualizar

`PUT /api/alunos/{id}`

Body: mesmo formato de Criar (entidade `Aluno` completa). O `id` da URL não é copiado automaticamente para o objeto antes de salvar — envie o `id` também no corpo se o backend depender dele (`AlunoService.atualizar` chama `buscarPorId(aluno.getId())`, usando o id do corpo, não o da URL).

`AlunoService.atualizar` roda proteções específicas para `usuario`:
- Se o corpo do `PUT` **não incluir** `usuario` (campo ausente/`null`), o backend preserva o `usuario` que o aluno já tinha, em vez de desvincular — isso evita que uma edição parcial (ex.: só atualizando `endereco`) apague o vínculo com o usuário sem querer.
- Se o corpo incluir um `usuario` com `id` de um usuário **já vinculado a outra pessoa**, retorna `400` ("Esse usuário já está vinculado a outra pessoa.").
- Tratamento de senha: mesma tabela da seção "Criar" acima — inclui o caso de **trocar a senha de um aluno existente**, reenviando `usuario.id` + a nova senha em texto puro.

Retorno: `200 OK`, `AuthResponse<Aluno>` (entidade crua).

---

## Excluir

`DELETE /api/alunos/{id}`

Retorno: `204 No Content`, `AuthResponse` só com `message` (sem `dado`).

## Observações

- Erros de "não encontrado" (`GET`/`PUT`/`DELETE` com id inexistente) resultam em **500**, não 404, porque o controller intercepta todas as exceções genericamente. Trate `500` como possível "não encontrado" neste endpoint.
- Listagem/Busca retornam `AlunoDTO` (enxuto); Criação/Atualização retornam a entidade `Aluno` completa (com `endereco`, `usuario`, `telefones`, `matriculas`, `historicos`). Não assuma o mesmo shape entre as operações.
