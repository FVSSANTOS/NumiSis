# Professor

Estende [Pessoa](pessoa.md) (campos `id`, `nome`, `cpf`, `idade`, `dataCadastro`, `dataNascimento`, `email`, `endereco`, `telefones`, `usuario`) — `usuario` é 100% herdado de `Pessoa` (`cascade = ALL`, mesmo padrão de [Aluno](aluno.md)); `Professor` não redeclara mais esse campo.

## Endpoint Base

`/api/professores`

## Autenticação e Permissões

`SecurityConfig`: `/api/professores/**` exige role `ADMIN` ou `PROFESSOR` para **todas** as operações (não há `@PreAuthorize` mais granular no controller).

`listar`/`buscar` retornam `ProfessorDTO` (via `ProfessorMapper`); `criar`/`atualizar` retornam a entidade `Professor` completa — mesmo padrão de [Aluno](aluno.md).

---

## Listar

`GET /api/professores`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Professores retornados com sucesso!",
  "dado": {
    "content": [ /* ProfessorDTO[] */ ],
    "page": 0,
    "size": 20,
    "totalElements": 5,
    "totalPages": 1
  }
}
```

### ProfessorDTO

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `nome` | string | |
| `cpf` | string | |
| `idade` | number | |
| `dataNascimento` | string (`yyyy-MM-dd`) | |
| `email` | string | |
| `cargaHoraria` | string | |
| `endereco` | objeto `Endereco` \| `null` | ver [endereco.md](endereco.md) |
| `telefones` | `Telefone[]` | ver [telefone.md](telefone.md) |
| `dadosBancarios` | objeto `DadosBancarios` \| `null` | ver [dados-bancarios.md](dados-bancarios.md) |
| `turmas` | `Turma[]` | turmas lecionadas pelo professor — ver [turma.md](turma.md) (lembrando que cada `Turma` tem `disciplina`/`professor` write-only, ocultos na resposta) |
| `usuarioLogin` | string \| `null` | apenas o `login` do `Usuario` vinculado — a senha (hash) **não** é exposta aqui |

Não inclui `dataCadastro` nem o objeto `Usuario` completo (só `usuarioLogin`).

---

## Buscar

`GET /api/professores/{id}`

Retorno: `200 OK`

```json
{ "message": "Professor encontrado com sucesso", "dado": { /* ProfessorDTO */ } }
```

---

## Criar

`POST /api/professores`

Body: entidade `Professor` completa.

| Campo | Tipo | Obrigatório | Observações |
|---|---|---|---|
| `nome`, `cpf`, `idade`, `dataNascimento`, `dataCadastro`, `email` | — | não (sem validações declaradas na entidade) | herdados de Pessoa |
| `endereco` | objeto `Endereco` | não | cascata via Pessoa |
| `usuario` | objeto `Usuario` (`login`, `senha` em texto puro, `role`) | não | persistido em cascata (`cascade = ALL`, herdado de Pessoa). `senha` passa pelo mesmo tratamento de [Aluno](aluno.md) — ver tabela abaixo. Um mesmo `Usuario` não pode ficar vinculado a mais de uma pessoa (`usuario_id` é `unique` na tabela `pessoa`, validado também em código) |
| `cargaHoraria` | string | não | |
| `dadosBancarios` | objeto `DadosBancarios` | não | persistido em cascata se enviado |

Não há validação de CPF duplicado no `ProfessorService` (diferente de Aluno). Há, porém, validação de `usuario` (`ProfessorService.salvar`, usado tanto em `criar` quanto em `atualizar`), retornando `400 Bad Request` via `catch (RegraNegocioException e)` específico no controller:
- `login` já existente e `usuario.id` nulo (usuário novo) → "Já existe um usuário com esse login."
- `usuario.id` preenchido referenciando um usuário **já vinculado a outra pessoa** → "Esse usuário já está vinculado a outra pessoa."

### Regra de senha do `usuario` embutido (`ProfessorService.tratarSenhaUsuario`, mesma lógica de [Aluno](aluno.md))

| Situação | Comportamento |
|---|---|
| `usuario.id` nulo (usuário novo) | `senha` enviada (texto puro) é criptografada com BCrypt |
| `usuario.id` preenchido **com** `senha` no corpo | `senha` enviada (texto puro) é criptografada com BCrypt — é assim que se troca a senha de um professor já existente |
| `usuario.id` preenchido **sem** `senha` no corpo | o hash atual é mantido (busca o `Usuario` no banco e reaproveita a senha) |

Nunca envie um hash já criptografado no campo `senha`.

Retorno: `201 Created`

```json
{ "message": "Professor salvo com sucesso!", "dado": { /* Professor completo */ } }
```

---

## Atualizar

`PUT /api/professores/{id}`

Body: mesmo formato de Criar. Assim como em Aluno, se o corpo **não incluir** `usuario`, o backend preserva o `usuario` que o professor já tinha (em vez de desvincular) — `ProfessorService.salvar` busca o professor existente e reaproveita o `usuario` quando o campo vem ausente/`null` no `PUT`.

Retorno: `200 OK`, `{ "message": "Professor atualizado com sucesso!", "dado": { /* Professor completo */ } }`.

---

## Excluir

`DELETE /api/professores/{id}`

Retorno: `204 No Content`, `{ "message": "Professor deletado com sucesso!" }`.

## Observações

- Id inexistente em `GET`/`DELETE` deveria retornar 404, mas como o `catch (Exception e)` genérico intercepta a `NaoEncontradoException` antes, o resultado real observado é **500**, com `dado` contendo a exceção serializada em vez de uma mensagem de "não encontrado".
- Listagem/Busca retornam `ProfessorDTO` (com `usuarioLogin`); Criação/Atualização retornam a entidade `Professor` completa (com o objeto `Usuario` inteiro, incluindo a senha em hash — ver aviso de segurança no `SKILL.md`). Não assuma o mesmo shape entre as operações.
