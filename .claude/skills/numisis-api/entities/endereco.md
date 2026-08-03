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

**Sem paginação, sem envelope.** Retorno: `200 OK`, array de `Endereco`.

---

## Buscar

`GET /api/enderecos/{id}`

**Sem envelope.** Retorno: `200 OK`, ou `404 Not Found` se não existir.

---

## Criar

`POST /api/enderecos`

Body: objeto `Endereco` (sem `id`).

**Sem envelope.** Retorno: `201 Created`.

---

## Atualizar

`PUT /api/enderecos/{id}`

**Sem envelope.** Retorno: `200 OK`.

---

## Excluir

`DELETE /api/enderecos/{id}`

**Sem envelope.** Retorno: `204 No Content`, ou `404 Not Found` se não existir.

## Observações

- Normalmente um `Endereco` é criado/atualizado embutido no corpo de `Aluno`/`Professor` (relação `@OneToOne(cascade = ALL)`), então este CRUD dedicado costuma ser usado apenas para consulta avulsa.
