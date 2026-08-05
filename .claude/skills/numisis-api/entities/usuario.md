# Usuario

Credenciais de login (usado por `Aluno`/`Professor` via relação `usuario`, e para autenticação em `/api/auth/login`).

## Endpoint Base

`/api/usuarios`

## Autenticação e Permissões

`SecurityConfig`: exige role `ADMIN` para **todas** as operações.

## Entidade Usuario

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | number | |
| `login` | string | único no banco |
| `senha` | string | ⚠️ retornada como **hash BCrypt** em toda resposta que inclua um `Usuario` — nunca exiba este campo na UI |
| `role` | `"ALUNO" \| "PROFESSOR" \| "ADMIN"` | enum `Role` |

---

## Listar

`GET /api/usuarios`

**Sem paginação.** Retorno: `200 OK`

```json
{ "message": "Usuários retornados com sucesso!", "dado": [ /* Usuario[] (inclui senha com hash) */ ] }
```

---

## Buscar

`GET /api/usuarios/{id}`

Retorno: `200 OK`, `{ "message": "Usuário encontrado com sucesso!", "dado": { /* Usuario */ } }`, ou `404 Not Found`, `{ "message": "Usuário não encontrado com id: {id}" }` se não existir.

---

## Criar

`POST /api/usuarios`

Body: `{ "login": string, "senha": string (texto puro), "role": "ALUNO" | "PROFESSOR" | "ADMIN" }`.

A senha enviada em texto puro é criptografada (BCrypt) pelo `UsuarioService` antes de salvar — diferente do que acontece ao enviar um `usuario` embutido em `POST /api/alunos` ou `/api/professores`, onde **não** há criptografia automática.

`login` duplicado → `RegraNegocioException` ("Já existe um usuário com esse login."), retornada como `400 Bad Request`, `{ "message": "Já existe um usuário com esse login." }` (checado via `existsByLogin` antes de salvar, só quando é criação — `usuario.id == null`).

Retorno (sucesso): `201 Created`, `{ "message": "Usuário salvo com sucesso!", "dado": { /* Usuario, senha já como hash */ } }`.

---

## Atualizar

`PUT /api/usuarios/{id}`

Mesmo body de Criar — **atenção:** todo `PUT` passa novamente pela criptografia (`usuario.setSenha(passwordEncoder.encode(usuario.getSenha()))`). Se o formulário de edição reenviar o hash existente (obtido de um `GET` anterior) sem que o usuário digite uma nova senha, o hash será criptografado novamente e a senha original deixará de funcionar. **O frontend deve sempre enviar uma senha em texto puro neste campo (nunca o hash recebido de um GET), ou omitir a atualização de senha via um fluxo separado.**

Retorno: `200 OK`, `{ "message": "Usuário atualizado com sucesso!", "dado": { /* Usuario */ } }`.

---

## Excluir

`DELETE /api/usuarios/{id}`

Retorno: `204 No Content`, `{ "message": "Usuário deletado com sucesso!" }`, ou `404 Not Found`, `{ "message": "Usuário não encontrado com id: {id}" }` se não existir.

## Observações

- Único endpoint da API restrito exclusivamente a `ADMIN`.
- Cuidado especial no formulário de edição de usuário por causa do re-hash de senha descrito acima.
