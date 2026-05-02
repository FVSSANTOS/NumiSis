package com.FVSS.numisis.controller;

import java.util.List;

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
import com.FVSS.numisis.domain.model.Disciplina;
import com.FVSS.numisis.dto.PageResponse;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.DisciplinaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    public ResponseEntity<Disciplina> criar(@Valid @RequestBody Disciplina disciplina) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaService.salvar(disciplina));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar(Pageable pageable) {
        try {
            Page<Disciplina> page = disciplinaService.listarTodos(pageable);
            List<Disciplina> disciplinas = page.getContent()
                                     .stream()
                                     .toList();
            
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Disciplinas retornadas com sucesso!",
            new PageResponse<>(
                disciplinas,
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
    public ResponseEntity<Disciplina> buscar(@PathVariable Long id) {
        try {
            return disciplinaService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disciplina> atualizar(@PathVariable Long id, @Valid @RequestBody Disciplina disciplina) {
        try {
            disciplina.setId(id);
            return ResponseEntity.ok(disciplinaService.salvar(disciplina));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            if (disciplinaService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            disciplinaService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
