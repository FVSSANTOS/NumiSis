# Autenticação

## Endpoint Base

`/api/auth`

## Autenticação e Permissões

Rota pública (`.requestMatchers("/api/auth/**").permitAll()`), não exige token.

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

- Credenciais inválidas: `AuthenticationManager.authenticate` lança exceção não tratada pelo `GlobalExceptionHandler` — cai no tratamento padrão do Spring Security, tipicamente `401 Unauthorized` (via `AuthenticationEntryPoint` configurado em `JwtAuthenticationEntryPoint`), corpo fora do padrão `AuthResponse`.
- `login`/`senha` em branco: `400 Bad Request` no formato padrão do Spring (`@Valid` sem handler customizado — ver seção de erros em `SKILL.md`), não no formato `AuthResponse`.

## Observações

- Este é o único método deste controller — não há `logout` (stateless/JWT: o frontend deve simplesmente descartar o token) nem `register` (usuários são criados via `POST /api/usuarios`, restrito a `ADMIN`, ou embutidos no corpo de `POST /api/alunos`/`/api/professores`).
