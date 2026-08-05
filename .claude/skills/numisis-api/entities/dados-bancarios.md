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

**Sem paginação.** Retorno: `200 OK`

```json
{ "message": "Dados bancários retornados com sucesso!", "dado": [ /* DadosBancarios[] */ ] }
```

---

## Buscar

`GET /api/dados-bancarios/{id}`

Retorno: `200 OK`, `{ "message": "Dados bancários encontrados com sucesso!", "dado": { /* DadosBancarios */ } }`, ou `404 Not Found`, `{ "message": "Dados bancários não encontrados com id: {id}" }` se não existir.

---

## Criar

`POST /api/dados-bancarios`

Body: `{ "banco": string, "agencia": string, "conta": string, "pix": string }`.

Retorno: `201 Created`, `{ "message": "Dados bancários salvos com sucesso!", "dado": { /* DadosBancarios */ } }`.

---

## Atualizar

`PUT /api/dados-bancarios/{id}`

Retorno: `200 OK`, `{ "message": "Dados bancários atualizados com sucesso!", "dado": { /* DadosBancarios */ } }`.

---

## Excluir

`DELETE /api/dados-bancarios/{id}`

Retorno: `204 No Content`, `{ "message": "Dados bancários deletados com sucesso!" }`, ou `404 Not Found`, `{ "message": "Dados bancários não encontrados com id: {id}" }` se não existir.

## Observações

- Como a relação em `Professor` é `cascade = ALL`, também é possível criar/atualizar `DadosBancarios` embutido no corpo de `POST/PUT /api/professores`, sem usar este endpoint diretamente.
