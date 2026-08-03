#!/usr/bin/env python3
"""Gera a collection Postman da API Numisis."""
import json

BASE_TESTS = [
    'pm.test("Tempo de resposta menor que 5s", function () {',
    '    pm.expect(pm.response.responseTime).to.be.below(5000);',
    '});',
]

JSON_HEADER_TEST = [
    'pm.test("Content-Type é JSON", function () {',
    '    pm.response.to.have.header("Content-Type");',
    '    pm.expect(pm.response.headers.get("Content-Type")).to.include("application/json");',
    '});',
]


def status_test(code, label=None):
    label = label or str(code)
    return [
        f'pm.test("Status code é {label}", function () {{',
        f"    pm.response.to.have.status({code});",
        "});",
    ]


def save_id_test(var_name, json_path="id"):
    parts = json_path.split(".")
    expr = "json"
    for part in parts:
        expr += f'.{part}'
    return [
        "if (pm.response.code >= 200 && pm.response.code < 300) {",
        "    const json = pm.response.json();",
        f"    if ({expr} !== undefined && {expr} !== null) {{",
        f'        pm.collectionVariables.set("{var_name}", String({expr}));',
        "    }",
        "}",
    ]


def make_request(name, method, path, raw_body=None, tests=None, description="", query=None, no_auth=False):
    url_path = path if path.startswith("/") else f"/{path}"
    segments = [p for p in url_path.strip("/").split("/") if p]
    url = {
        "raw": "{{baseUrl}}" + url_path + ("?" + "&".join(f"{k}={v}" for k, v in query.items()) if query else ""),
        "host": ["{{baseUrl}}"],
        "path": segments,
    }
    if query:
        url["query"] = [{"key": k, "value": v} for k, v in query.items()]

    req = {
        "name": name,
        "request": {
            "method": method,
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": url,
            "description": description,
        },
        "response": [],
    }

    if no_auth:
        req["request"]["auth"] = {"type": "noauth"}

    if raw_body is not None:
        req["request"]["body"] = {"mode": "raw", "raw": raw_body}

    if tests:
        req["event"] = [{"listen": "test", "script": {"exec": tests, "type": "text/javascript"}}]

    return req


def crud_folder(name, base_path, create_body, update_body, id_var, list_query=None, create_status=201, extra_create_tests=None):
    create_tests = status_test(create_status) + BASE_TESTS + JSON_HEADER_TEST + save_id_test(id_var)
    if extra_create_tests:
        create_tests += extra_create_tests
    else:
        create_tests += [
            f'pm.test("Resposta contém id", function () {{',
            f"    pm.expect(pm.response.json()).to.have.property('id');",
            "});",
        ]

    list_tests = status_test(200) + BASE_TESTS + JSON_HEADER_TEST
    get_tests = status_test(200) + BASE_TESTS + JSON_HEADER_TEST + [
        f'pm.test("ID retornado corresponde", function () {{',
        f'    pm.expect(pm.response.json().id).to.eql(Number(pm.collectionVariables.get("{id_var}")));',
        "});",
    ]
    update_tests = status_test(200) + BASE_TESTS + JSON_HEADER_TEST
    delete_tests = status_test(204) + BASE_TESTS

    return {
        "name": name,
        "item": [
            make_request(f"Criar {name}", "POST", base_path, create_body, create_tests, f"Cria um novo registro de {name}."),
            make_request(
                f"Listar {name}",
                "GET",
                base_path,
                tests=list_tests,
                description=f"Lista registros de {name}.",
                query=list_query,
            ),
            make_request(
                f"Buscar {name} por ID",
                "GET",
                f"{base_path}/{{{{{id_var}}}}}",
                tests=get_tests,
                description=f"Busca {name} pelo identificador.",
            ),
            make_request(
                f"Atualizar {name}",
                "PUT",
                f"{base_path}/{{{{{id_var}}}}}",
                update_body,
                update_tests,
                f"Atualiza {name} existente.",
            ),
            make_request(
                f"Remover {name}",
                "DELETE",
                f"{base_path}/{{{{{id_var}}}}}",
                tests=delete_tests,
                description=f"Remove {name} pelo identificador.",
            ),
        ],
    }


def build_collection():
    collection = {
        "info": {
            "_postman_id": "a1b2c3d4-numisis-api-2026",
            "name": "Numisis API",
            "description": (
                "Collection completa da API Numisis com testes automatizados.\n\n"
                "**Como usar:**\n"
                "1. Importe este arquivo no Postman (Import > Upload Files).\n"
                "2. Execute **Autenticação > Login Admin** para obter o token JWT.\n"
                "3. As demais rotas usam Bearer Token automaticamente via variável `token`.\n"
                "4. Requests de criação salvam IDs nas variáveis da collection.\n\n"
                "**Ordem sugerida para testes completos:**\n"
                "Login → Usuários → Endereços → Dados Bancários → Cursos → Disciplinas → "
                "Cursos-Disciplinas → Professores → Alunos → Turmas → Matrículas → Históricos → Telefones\n\n"
                "Credenciais padrão: login `admin`, senha `admin123`."
            ),
            "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
        },
        "auth": {
            "type": "bearer",
            "bearer": [{"key": "token", "value": "{{token}}", "type": "string"}],
        },
        "variable": [
            {"key": "baseUrl", "value": "http://localhost:8080"},
            {"key": "token", "value": ""},
            {"key": "usuarioId", "value": "1"},
            {"key": "enderecoId", "value": "1"},
            {"key": "dadosBancariosId", "value": "1"},
            {"key": "cursoId", "value": "1"},
            {"key": "disciplinaId", "value": "1"},
            {"key": "cursoDisciplinaId", "value": "1"},
            {"key": "professorId", "value": "1"},
            {"key": "alunoId", "value": "1"},
            {"key": "turmaId", "value": "1"},
            {"key": "matriculaId", "value": "1"},
            {"key": "historicoId", "value": "1"},
            {"key": "telefoneId", "value": "1"},
            {"key": "pessoaId", "value": "1"},
        ],
        "item": [],
    }

    login_tests = (
        status_test(200)
        + BASE_TESTS
        + JSON_HEADER_TEST
        + [
            'pm.test("Resposta contém token JWT", function () {',
            "    const json = pm.response.json();",
            '    pm.expect(json.dado).to.have.property("token");',
            '    pm.expect(json.dado.token).to.be.a("string").and.not.empty;',
            "});",
            'pm.test("Mensagem de sucesso", function () {',
            "    pm.expect(pm.response.json().message).to.eql('Login realizado com sucesso');",
            "});",
        ]
        + save_id_test("token", "dado.token")
        + save_id_test("usuarioId", "dado.id")
    )

    auth = {
        "name": "Autenticação",
        "item": [
            make_request(
                "Login Admin",
                "POST",
                "/api/auth/login",
                json.dumps({"login": "admin", "senha": "admin123"}, indent=2),
                login_tests,
                "Autentica com o usuário admin padrão.",
                no_auth=True,
            )
        ],
    }

    aluno_create_tests = [
        'pm.test("Mensagem de sucesso", function () {',
        "    pm.expect(pm.response.json().message).to.eql('Aluno salvo com sucesso!');",
        "});",
    ]

    aluno_list_tests = status_test(200) + BASE_TESTS + JSON_HEADER_TEST + [
        'pm.test("Resposta paginada contém dados", function () {',
        "    const json = pm.response.json();",
        '    pm.expect(json.dado).to.have.property("content");',
        "});",
        "const json = pm.response.json();",
        "if (json.dado && json.dado.content && json.dado.content.length > 0) {",
        '    pm.collectionVariables.set("alunoId", String(json.dado.content[0].id));',
        '    pm.collectionVariables.set("pessoaId", String(json.dado.content[0].id));',
        "}",
    ]

    aluno_get_tests = status_test(200) + BASE_TESTS + JSON_HEADER_TEST + [
        'pm.test("Resposta contém dados do aluno", function () {',
        "    pm.expect(pm.response.json().dado).to.have.property('nome');",
        "});",
    ]

    aluno_folder = {
        "name": "Alunos",
        "item": [
            make_request(
                "Criar Aluno",
                "POST",
                "/api/alunos",
                json.dumps({
                    "nome": "Maria Aluna",
                    "cpf": "52998224725",
                    "idade": 20,
                    "dataNascimento": "2005-06-20",
                    "nomeMae": "Ana Aluna",
                    "nomePai": "Carlos Aluno",
                    "condicaoEspecial": "Nenhuma",
                    "alergia": "Nenhuma",
                    "usuario": {"id": "{{usuarioId}}"},
                    "endereco": {"id": "{{enderecoId}}"},
                }, indent=2).replace('"{{usuarioId}}"', "{{usuarioId}}").replace('"{{enderecoId}}"', "{{enderecoId}}"),
                status_test(201) + BASE_TESTS + JSON_HEADER_TEST + aluno_create_tests,
                "Cria um novo aluno.",
            ),
            make_request("Listar Alunos", "GET", "/api/alunos", tests=aluno_list_tests, query={"page": "0", "size": "10"}),
            make_request("Buscar Aluno por ID", "GET", "/api/alunos/{{alunoId}}", tests=aluno_get_tests),
            make_request(
                "Atualizar Aluno",
                "PUT",
                "/api/alunos/{{alunoId}}",
                json.dumps({
                    "nome": "Maria Aluna Santos",
                    "cpf": "52998224725",
                    "idade": 21,
                    "dataNascimento": "2005-06-20",
                    "nomeMae": "Ana Aluna",
                    "nomePai": "Carlos Aluno",
                    "condicaoEspecial": "Nenhuma",
                    "alergia": "Amendoim",
                    "usuario": {"id": "{{usuarioId}}"},
                    "endereco": {"id": "{{enderecoId}}"},
                }, indent=2).replace('"{{usuarioId}}"', "{{usuarioId}}").replace('"{{enderecoId}}"', "{{enderecoId}}"),
                status_test(200) + BASE_TESTS + JSON_HEADER_TEST,
            ),
            make_request("Remover Aluno", "DELETE", "/api/alunos/{{alunoId}}", tests=status_test(204) + BASE_TESTS),
        ],
    }

    def fix_ids(raw):
        for var in [
            "usuarioId", "enderecoId", "dadosBancariosId", "cursoId", "disciplinaId",
            "professorId", "alunoId", "turmaId", "pessoaId",
        ]:
            raw = raw.replace(f'"{{{{{var}}}}}"', f"{{{{{var}}}}}")
        return raw

    folders = [
        auth,
        crud_folder(
            "Usuários",
            "/api/usuarios",
            json.dumps({"login": "usuario_teste", "senha": "senha123", "role": "ALUNO"}, indent=2),
            json.dumps({"login": "usuario_teste_alt", "senha": "senha456", "role": "PROFESSOR"}, indent=2),
            "usuarioId",
        ),
        crud_folder(
            "Endereços",
            "/api/enderecos",
            json.dumps({
                "numero": "100", "rua": "Rua das Flores", "bairro": "Centro",
                "cidade": "São Paulo", "cep": "01001000", "complemento": "Apto 12",
            }, indent=2, ensure_ascii=False),
            json.dumps({
                "numero": "200", "rua": "Rua das Flores", "bairro": "Centro",
                "cidade": "São Paulo", "cep": "01001000", "complemento": "Sala 3",
            }, indent=2, ensure_ascii=False),
            "enderecoId",
        ),
        crud_folder(
            "Dados Bancários",
            "/api/dados-bancarios",
            json.dumps({"banco": "Banco do Brasil", "agencia": "1234", "conta": "56789-0", "pix": "admin@email.com"}, indent=2),
            json.dumps({"banco": "Banco do Brasil", "agencia": "1234", "conta": "99999-1", "pix": "admin@email.com"}, indent=2),
            "dadosBancariosId",
        ),
        crud_folder(
            "Cursos",
            "/api/cursos",
            json.dumps({"nome": "Engenharia de Software", "descricao": "Curso de graduação em ES"}, indent=2, ensure_ascii=False),
            json.dumps({"nome": "Engenharia de Software - Atualizado", "descricao": "Descrição atualizada"}, indent=2, ensure_ascii=False),
            "cursoId",
            {"page": "0", "size": "10"},
        ),
        crud_folder(
            "Disciplinas",
            "/api/disciplinas",
            json.dumps({"nome": "Programação Orientada a Objetos", "descricao": "Disciplina de POO"}, indent=2, ensure_ascii=False),
            json.dumps({"nome": "POO Avançada", "descricao": "Conteúdo avançado de POO"}, indent=2, ensure_ascii=False),
            "disciplinaId",
            {"page": "0", "size": "10"},
        ),
        crud_folder(
            "Cursos-Disciplinas",
            "/api/cursos-disciplinas",
            fix_ids(json.dumps({"curso": {"id": "{{cursoId}}"}, "disciplina": {"id": "{{disciplinaId}}"}}, indent=2)),
            fix_ids(json.dumps({"curso": {"id": "{{cursoId}}"}, "disciplina": {"id": "{{disciplinaId}}"}}, indent=2)),
            "cursoDisciplinaId",
        ),
        crud_folder(
            "Professores",
            "/api/professores",
            fix_ids(json.dumps({
                "nome": "João Professor", "cpf": "39053344705", "idade": 40,
                "dataNascimento": "1985-03-15", "cargaHoraria": "40h",
                "usuario": {"id": "{{usuarioId}}"},
                "dadosBancarios": {"id": "{{dadosBancariosId}}"},
            }, indent=2)),
            fix_ids(json.dumps({
                "nome": "João Professor Silva", "cpf": "39053344705", "idade": 41,
                "dataNascimento": "1985-03-15", "cargaHoraria": "20h",
                "usuario": {"id": "{{usuarioId}}"},
                "dadosBancarios": {"id": "{{dadosBancariosId}}"},
            }, indent=2)),
            "professorId",
            {"page": "0", "size": "10"},
        ),
        aluno_folder,
        crud_folder(
            "Turmas",
            "/api/turmas",
            fix_ids(json.dumps({
                "ano": 2026, "semestre": 1, "sala": "A101",
                "horarioInicio": "08:00", "horarioTermino": "10:00",
                "disciplina": {"id": "{{disciplinaId}}"},
                "professor": {"id": "{{professorId}}"},
            }, indent=2)),
            fix_ids(json.dumps({
                "ano": 2026, "semestre": 1, "sala": "B202",
                "horarioInicio": "08:00", "horarioTermino": "11:00",
                "disciplina": {"id": "{{disciplinaId}}"},
                "professor": {"id": "{{professorId}}"},
            }, indent=2)),
            "turmaId",
            {"page": "0", "size": "10"},
        ),
        crud_folder(
            "Matrículas",
            "/api/matriculas",
            fix_ids(json.dumps({
                "dataMatricula": "2026-02-01", "situacao": "ATIVA",
                "aluno": {"id": "{{alunoId}}"}, "curso": {"id": "{{cursoId}}"},
            }, indent=2)),
            fix_ids(json.dumps({
                "dataMatricula": "2026-02-01", "situacao": "TRANCADA",
                "aluno": {"id": "{{alunoId}}"}, "curso": {"id": "{{cursoId}}"},
            }, indent=2)),
            "matriculaId",
        ),
        crud_folder(
            "Históricos Disciplinas",
            "/api/historicos-disciplinas",
            fix_ids(json.dumps({
                "nota": 8.5, "faltas": 2, "ano": 2026, "semestre": 1,
                "situacao": "APROVADO",
                "aluno": {"id": "{{alunoId}}"}, "turma": {"id": "{{turmaId}}"},
            }, indent=2)),
            fix_ids(json.dumps({
                "nota": 9.0, "faltas": 1, "ano": 2026, "semestre": 1,
                "situacao": "APROVADO",
                "aluno": {"id": "{{alunoId}}"}, "turma": {"id": "{{turmaId}}"},
            }, indent=2)),
            "historicoId",
            {"page": "0", "size": "10"},
        ),
        {
            "name": "Pessoas",
            "item": [
                make_request("Listar Pessoas", "GET", "/api/pessoas", tests=status_test(200) + BASE_TESTS + JSON_HEADER_TEST),
                make_request("Buscar Pessoa por ID", "GET", "/api/pessoas/{{pessoaId}}", tests=status_test(200) + BASE_TESTS + JSON_HEADER_TEST),
                make_request("Remover Pessoa", "DELETE", "/api/pessoas/{{pessoaId}}", tests=status_test(204) + BASE_TESTS),
            ],
        },
        crud_folder(
            "Telefones",
            "/api/telefones",
            fix_ids(json.dumps({"numero": "11999998888", "tipo": "CELULAR", "pessoa": {"id": "{{pessoaId}}"}}, indent=2)),
            fix_ids(json.dumps({"numero": "11988887777", "tipo": "RESIDENCIAL", "pessoa": {"id": "{{pessoaId}}"}}, indent=2)),
            "telefoneId",
        ),
    ]

    collection["item"] = folders
    return collection


if __name__ == "__main__":
    import os
    output = build_collection()
    script_dir = os.path.dirname(os.path.abspath(__file__))
    path = os.path.join(script_dir, "Numisis-API.postman_collection.json")
    with open(path, "w", encoding="utf-8") as f:
        json.dump(output, f, indent=2, ensure_ascii=False)
    print(f"Collection gerada: {path}")
