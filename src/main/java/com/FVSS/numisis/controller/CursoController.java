package com.FVSS.numisis.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.FVSS.numisis.domain.model.Curso;
import com.FVSS.numisis.dto.PageResponse;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.CursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody Curso curso) {
        try {
            var cursoSalvo = cursoService.salvar(curso);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Curso salvo com sucesso!", cursoSalvo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar(Pageable pageable) {
        try {
            Page<Curso> page = cursoService.listarTodos(pageable);
            List<Curso> cursos = page.getContent()
                                     .stream()
                                     .toList();

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Cursos retornados com sucesso!",
            new PageResponse<>(
                cursos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages())
             ));
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new AuthResponse<>(
                                     "Erro no processamento do servidor", e)
                                  );
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> buscar(@PathVariable Long id) {
        try {
            Optional<Curso> cursoEncontrado = cursoService.buscarPorId(id);
            if (cursoEncontrado.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Curso não encontrado com id: " + id));
            }
            return ResponseEntity.ok(new AuthResponse<>("Curso encontrado com sucesso!", cursoEncontrado.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id, @Valid @RequestBody Curso curso) {
        try {
            curso.setId(id);
            var cursoAtualizado = cursoService.salvar(curso);
            return ResponseEntity.ok(new AuthResponse<>("Curso atualizado com sucesso!", cursoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            if (cursoService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Curso não encontrado com id: " + id));
            }
            cursoService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Curso deletado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
