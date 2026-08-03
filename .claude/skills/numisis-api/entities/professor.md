# Professor

Estende [Pessoa](pessoa.md) (campos `id`, `nome`, `cpf`, `idade`, `dataCadastro`, `dataNascimento`, `email`, `endereco`, `telefones`, `usuario`).

> A classe `Professor` redeclara o campo `usuario` (mesma coluna `usuario_id`, mas sem `cascade`) — na prática funciona como o `usuario` de `Pessoa`, só não é salvo em cascata ao salvar o professor.

## Endpoint Base

`/api/professores`

## Autenticação e Permissões

`SecurityConfig`: `/api/professores/**` exige role `ADMIN` ou `PROFESSOR` para **todas** as operações (não há `@PreAuthorize` mais granular no controller).

**Todos os endpoints retornam a entidade `Professor` completa — não existe uso do `ProfessorDTO`/`ProfessorMapper` no controller atual**, apesar de esses arquivos existirem no projeto.

---

## Listar

`GET /api/professores`

Query params de paginação: `page`, `size`, `sort`.

Retorno: `200 OK`

```json
{
  "message": "Históricos retornados com sucesso!",
  "dado": {
    "content": [ /* Professor[] (entidade completa) */ ],
    "page": 0,
    "size": 20,
    "totalElements": 5,
    "totalPages": 1
  }
}
```

> A mensagem "Históricos retornados com sucesso!" está incorreta no backend (copiado de outro endpoint) — ignore o texto, use apenas `dado`.

### Campos de Professor (entidade completa, além dos herdados de Pessoa)

| Campo | Tipo | Descrição |
|---|---|---|
| `cargaHoraria` | string | |
| `dadosBancarios` | objeto `DadosBancarios` \| null | `@OneToOne(cascade = ALL)` — ver [dados-bancarios.md](dados-bancarios.md) |
| `turmas` | `Turma[]` | lista de turmas lecionadas |

---

## Buscar

`GET /api/professores/{id}`

Retorno: `200 OK`

```json
{ "message": "Professor encontrado com sucesso", "dado": { /* Professor completo */ } }
```

---

## Criar

`POST /api/professores`

Body: entidade `Professor` completa.

| Campo | Tipo | Obrigatório | Observações |
|---|---|---|---|
| `nome`, `cpf`, `idade`, `dataNascimento`, `dataCadastro`, `email` | — | não (sem validações declaradas na entidade) | herdados de Pessoa |
| `endereco` | objeto `Endereco` | não | cascata via Pessoa |
| `usuario` | objeto `Usuario` | não | **não** é salvo em cascata (ver nota acima) — cadastre o usuário separadamente em `/api/usuarios` e referencie pelo `id`, ou espere que o vínculo não persista |
| `cargaHoraria` | string | não | |
| `dadosBancarios` | objeto `DadosBancarios` | não | persistido em cascata se enviado |

Não há validação de CPF duplicado no `ProfessorService` (diferente de Aluno).

Retorno: `201 Created`

```json
{ "message": "Professor salvo com sucesso!", "dado": { /* Professor completo */ } }
```

---

## Atualizar

`PUT /api/professores/{id}`

Body: mesmo formato de Criar. **Sem envelope** (diferente dos demais métodos deste controller):

Retorno: `200 OK`, corpo é o `Professor` cru (sem `message`/`dado`).

---

## Excluir

`DELETE /api/professores/{id}`

**Sem envelope.** Retorno: `204 No Content` (corpo vazio).

## Observações

- Shape inconsistente dentro do próprio controller: `criar`/`listar`/`buscar` usam `AuthResponse<Professor>`; `atualizar`/`remover` retornam o `Professor` cru / vazio, sem envelope. Verifique sempre qual método está chamando.
- Id inexistente em `GET`/`DELETE` deveria retornar 404, mas como o `catch (Exception e)` genérico intercepta a `NaoEncontradoException` antes, o resultado real é **500**.
