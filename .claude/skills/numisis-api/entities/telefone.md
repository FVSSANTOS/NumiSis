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

**Sem paginação, sem envelope.** Retorno: `200 OK`, array de `Telefone`.

---

## Buscar

`GET /api/telefones/{id}`

**Sem envelope.** Retorno: `200 OK`, ou `404 Not Found` se não existir.

---

## Criar

`POST /api/telefones`

Body: `{ "numero": string, "tipo": string, "pessoa": { "id": number } }` — para vincular a um aluno/professor existente, envie `pessoa.id`.

**Sem envelope.** Retorno: `201 Created`.

---

## Atualizar

`PUT /api/telefones/{id}`

**Sem envelope.** Retorno: `200 OK`.

---

## Excluir

`DELETE /api/telefones/{id}`

**Sem envelope.** Retorno: `204 No Content`, ou `404 Not Found` se não existir.

## Observações

- A relação `Pessoa.telefones` (`@OneToMany`, sem cascade) não é populada automaticamente ao criar um Aluno/Professor — cadastre telefones aqui, referenciando `pessoa.id`, depois de a pessoa já existir.
