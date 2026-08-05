# Telefone

## Endpoint Base

`/api/telefones`

## Autenticação e Permissões

Sem regra específica em `SecurityConfig` → cai em "qualquer usuário autenticado".

## Entidade Telefone

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `numero` | string | |
| `tipo` | string | texto livre (ex.: "celular", "fixo") — sem enum |
| `pessoa` | objeto `Pessoa` (Aluno/Professor) \| `null` | relação `@ManyToOne` — **sem `@JsonIgnore`**, então aparece na resposta e pode aninhar a pessoa inteira |

Sem validações declaradas.

---

## Listar

`GET /api/telefones`

**Sem paginação.** Retorno: `200 OK`

```json
{ "message": "Telefones retornados com sucesso!", "dado": [ /* Telefone[] */ ] }
```

---

## Buscar

`GET /api/telefones/{id}`

Retorno: `200 OK`, `{ "message": "Telefone encontrado com sucesso!", "dado": { /* Telefone */ } }`, ou `404 Not Found`, `{ "message": "Telefone não encontrado com id: {id}" }` se não existir.

---

## Criar

`POST /api/telefones`

Body: `{ "numero": string, "tipo": string, "pessoa": { "id": number } }` — para vincular a um aluno/professor existente, envie `pessoa.id`.

Retorno: `201 Created`, `{ "message": "Telefone salvo com sucesso!", "dado": { /* Telefone */ } }`.

---

## Atualizar

`PUT /api/telefones/{id}`

Retorno: `200 OK`, `{ "message": "Telefone atualizado com sucesso!", "dado": { /* Telefone */ } }`.

---

## Excluir

`DELETE /api/telefones/{id}`

Retorno: `204 No Content`, `{ "message": "Telefone deletado com sucesso!" }`, ou `404 Not Found`, `{ "message": "Telefone não encontrado com id: {id}" }` se não existir.

## Observações

- A relação `Pessoa.telefones` (`@OneToMany(cascade = ALL, orphanRemoval = true)`) também pode ser gerenciada embutindo a lista `telefones` no corpo de `POST`/`PUT /api/alunos` e `/api/professores` — nesse caso os telefones são criados/atualizados/removidos em cascata junto com a pessoa (remover um item da lista ao editar exclui o telefone). Este endpoint (`/api/telefones`) continua útil para CRUD avulso ou quando a pessoa já existe e você só quer adicionar/editar um telefone sem reenviar o objeto Aluno/Professor inteiro.
