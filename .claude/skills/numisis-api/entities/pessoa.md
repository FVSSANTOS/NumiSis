# Pessoa

Classe base abstrata (JPA `@Inheritance(strategy = JOINED)`) de `Aluno` e `Professor`. Não é criada diretamente pela API — o endpoint `/api/pessoas` serve apenas para consulta/exclusão de qualquer `Pessoa` (aluno ou professor) de forma genérica.

## Endpoint Base

`/api/pessoas`

## Autenticação e Permissões

Sem regra específica em `SecurityConfig` para `/api/pessoas/**` → cai em "qualquer usuário autenticado" (`.requestMatchers("/api/**").authenticated()`).

## Campos comuns herdados por Aluno e Professor

| Campo | Tipo | Observações |
|---|---|---|
| `id` | number | gerado pelo banco (`IDENTITY`) |
| `nome` | string | |
| `cpf` | string | validado com `@CPF` (Hibernate Validator), único no banco |
| `idade` | number | |
| `dataCadastro` | string (ISO datetime) | |
| `dataNascimento` | string (`yyyy-MM-dd`) | |
| `email` | string | |
| `endereco` | objeto `Endereco` \| `null` | relação `@OneToOne(cascade = ALL)` — se enviado no corpo ao criar Aluno/Professor, é persistido junto |
| `telefones` | `Telefone[]` | relação `@OneToMany(mappedBy = "pessoa")`, sem cascade — não é persistida automaticamente enviando junto no corpo de Aluno/Professor; cadastre via `/api/telefones` |
| `usuario` | objeto `Usuario` \| `null` | relação `@OneToOne(cascade = ALL)` — inclui `login`, `senha` (hash) e `role`; ver aviso de segurança no `SKILL.md` |

Não há DTO específico para `Pessoa` — os endpoints abaixo retornam a entidade completa (subtipo real, `Aluno` ou `Professor`, com os campos específicos de cada um incluídos).

---

## Listar

`GET /api/pessoas`

Retorno: `200 OK`, array simples, **sem paginação e sem envelope**:

```json
[ { "id": 1, "nome": "...", ... }, ... ]
```

---

## Buscar

`GET /api/pessoas/{id}`

Retorno: `200 OK` com a `Pessoa` (Aluno ou Professor), ou `404 Not Found` (corpo vazio) se não existir.

---

## Excluir

`DELETE /api/pessoas/{id}`

Retorno: `204 No Content`, ou `404 Not Found` se o id não existir.

## Observações

- Não existe `POST`/`PUT` neste controller — criação e atualização de pessoas acontecem através de `/api/alunos` ou `/api/professores`.
