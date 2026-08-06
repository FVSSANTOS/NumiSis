# Autenticação

## Endpoint Base

`/api/auth`

## Autenticação e Permissões

Rota pública (`.requestMatchers("/api/auth/**").permitAll()`), não exige token.

⚠️ **Nunca envie um header `Authorization: Bearer <token>` leftover/expirado em `POST /api/auth/login`** (ex.: um interceptor global de HTTP client que sempre anexa o último token salvo, mesmo na própria chamada de login). O `JwtAuthenticationFilter` roda em **toda** requisição, inclusive nas públicas — se vier um `Authorization` header, ele tenta validar aquele token *antes* de o request chegar no controller de login, independente do que estiver no corpo (`login`/`senha`). Se o token referenciar um usuário que não existe mais (renomeado/excluído) ou estiver corrompido, o filtro captura o erro e apenas segue sem autenticar (não derruba a requisição) — mas o efeito colateral é que, se você estiver testando erros de login, um token velho no header pode mascarar o que está de fato acontecendo. Para a chamada de login, não envie `Authorization` (ou garanta que o client limpa esse header antes de logar).

---

## Login

`POST /api/auth/login`

### LoginRequest (body)

| Campo | Tipo | Obrigatório | Observações |
|---|---|---|---|
| `login` | string | sim (`@NotBlank`) | mensagem: "O login é obrigatório" |
| `senha` | string | sim (`@NotBlank`) | mensagem: "A senha é obrigatória"; texto puro (comparado contra o hash via `AuthenticationManager`) |

### Retorno

`200 OK`

```json
{
  "message": "Login realizado com sucesso",
  "dado": {
    "token": "eyJ...",
    "tipo": "Bearer",
    "id": 1,
    "login": "admin",
    "role": "ADMIN"
  }
}
```

- `dado.token`: JWT — envie em requisições subsequentes como header `Authorization: Bearer {token}`.
- `dado.role`: `"ALUNO" | "PROFESSOR" | "ADMIN"` — use para decidir quais telas/ações mostrar no frontend (ver matriz de roles em `SKILL.md`).
- O token expira em 24h (`security.jwt.expiration-ms=86400000` em `application.properties`); não há endpoint de refresh — ao expirar, o usuário precisa logar novamente.

### Erros

- Credenciais inválidas (login não encontrado ou senha errada): `AuthenticationController.login` tem `try/catch` em volta de `authenticationManager.authenticate(...)`, então retorna `400 Bad Request`, `{ "message": "Erro ao realizar login: <mensagem da exceção>", "dado": null }` — dentro do formato `AuthResponse`, diferente do resto da API que costuma usar 401/404 para esse tipo de erro.
- `login`/`senha` em branco: `400 Bad Request` no formato padrão do Spring (`@Valid` sem handler customizado — ver seção de erros em `SKILL.md`), não no formato `AuthResponse`.

## Observações

- Este é o único método deste controller — não há `logout` (stateless/JWT: o frontend deve simplesmente descartar o token) nem `register` (usuários são criados via `POST /api/usuarios`, restrito a `ADMIN`, ou embutidos no corpo de `POST /api/alunos`/`/api/professores`).
