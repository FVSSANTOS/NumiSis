package com.FVSS.numisis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FVSS.numisis.domain.model.CursoDisciplina;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.CursoDisciplinaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cursos-disciplinas")
public class CursoDisciplinaController {

    private final CursoDisciplinaService cursoDisciplinaService;

    public CursoDisciplinaController(CursoDisciplinaService cursoDisciplinaService) {
        this.cursoDisciplinaService = cursoDisciplinaService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody CursoDisciplina cursoDisciplina) {
        try {
            var vinculoSalvo = cursoDisciplinaService.salvar(cursoDisciplina);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Vínculo entre curso e disciplina salvo com sucesso!", vinculoSalvo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar() {
        try {
            return ResponseEntity.ok(new AuthResponse<>(
                    "Vínculos entre cursos e disciplinas retornados com sucesso!", cursoDisciplinaService.listarTodos()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> buscar(@PathVariable Long id) {
        try {
            var disciplina = cursoDisciplinaService.buscarPorId(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AuthResponse<>("Vínculo entre curso e disciplina encontrado com sucesso!", disciplina));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id,
            @Valid @RequestBody CursoDisciplina cursoDisciplina) {
        try {
            cursoDisciplina.setId(id);
            var vinculoAtualizado = cursoDisciplinaService.salvar(cursoDisciplina);
            return ResponseEntity.ok(new AuthResponse<>("Vínculo entre curso e disciplina atualizado com sucesso!", vinculoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            if (cursoDisciplinaService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Vínculo entre curso e disciplina não encontrado com id: " + id));
            }
            cursoDisciplinaService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Vínculo entre curso e disciplina deletado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
