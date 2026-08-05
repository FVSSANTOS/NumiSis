# Endereco

## Endpoint Base

`/api/enderecos`

## Autenticação e Permissões

Sem regra específica em `SecurityConfig` → cai em "qualquer usuário autenticado".

## Entidade Endereco

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `numero` | string | |
| `rua` | string | |
| `bairro` | string | |
| `cidade` | string | |
| `cep` | string | |
| `complemento` | string | |

Sem validações declaradas (`@NotBlank`/etc.) em nenhum campo.

---

## Listar

`GET /api/enderecos`

**Sem paginação.** Retorno: `200 OK`

```json
{ "message": "Endereços retornados com sucesso!", "dado": [ /* Endereco[] */ ] }
```

---

## Buscar

`GET /api/enderecos/{id}`

Retorno: `200 OK`, `{ "message": "Endereço encontrado com sucesso!", "dado": { /* Endereco */ } }`, ou `404 Not Found`, `{ "message": "Endereço não encontrado com id: {id}" }` se não existir.

---

## Criar

`POST /api/enderecos`

Body: objeto `Endereco` (sem `id`).

Retorno: `201 Created`, `{ "message": "Endereço salvo com sucesso!", "dado": { /* Endereco */ } }`.

---

## Atualizar

`PUT /api/enderecos/{id}`

Retorno: `200 OK`, `{ "message": "Endereço atualizado com sucesso!", "dado": { /* Endereco */ } }`.

---

## Excluir

`DELETE /api/enderecos/{id}`

Retorno: `204 No Content`, `{ "message": "Endereço deletado com sucesso!" }`, ou `404 Not Found`, `{ "message": "Endereço não encontrado com id: {id}" }` se não existir.

## Observações

- Normalmente um `Endereco` é criado/atualizado embutido no corpo de `Aluno`/`Professor` (relação `@OneToOne(cascade = ALL)`), então este CRUD dedicado costuma ser usado apenas para consulta avulsa.
