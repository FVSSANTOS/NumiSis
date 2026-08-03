# DadosBancarios

Dados bancários de um [Professor](professor.md) (relação `@OneToOne(cascade = ALL)` em `Professor.dadosBancarios`).

## Endpoint Base

`/api/dados-bancarios`

## Autenticação e Permissões

Sem regra específica em `SecurityConfig` → cai em "qualquer usuário autenticado".

## Entidade DadosBancarios

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `banco` | string | |
| `agencia` | string | |
| `conta` | string | |
| `pix` | string | |

Sem validações declaradas.

---

## Listar

`GET /api/dados-bancarios`

**Sem paginação, sem envelope.** Retorno: `200 OK`, array de `DadosBancarios`.

---

## Buscar

`GET /api/dados-bancarios/{id}`

**Sem envelope.** Retorno: `200 OK`, ou `404 Not Found` se não existir.

---

## Criar

`POST /api/dados-bancarios`

Body: `{ "banco": string, "agencia": string, "conta": string, "pix": string }`.

**Sem envelope.** Retorno: `201 Created`.

---

## Atualizar

`PUT /api/dados-bancarios/{id}`

**Sem envelope.** Retorno: `200 OK`.

---

## Excluir

`DELETE /api/dados-bancarios/{id}`

**Sem envelope.** Retorno: `204 No Content`, ou `404 Not Found` se não existir.

## Observações

- Como a relação em `Professor` é `cascade = ALL`, também é possível criar/atualizar `DadosBancarios` embutido no corpo de `POST/PUT /api/professores`, sem usar este endpoint diretamente.
