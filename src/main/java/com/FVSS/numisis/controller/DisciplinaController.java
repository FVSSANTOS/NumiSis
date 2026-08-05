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

import com.FVSS.numisis.domain.model.Disciplina;
import com.FVSS.numisis.dto.DisciplinaDTO;
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
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody Disciplina disciplina) {
        try {
            var disciplinaSalva = disciplinaService.salvar(disciplina);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Disciplina salva com sucesso!", disciplinaSalva));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar(Pageable pageable) {
        try {
            Page<Disciplina> page = disciplinaService.listarTodos(pageable);
            List<Disciplina> disciplinas = page.getContent()
                                     .stream()
                                     .toList();
            var disciplinasDTO = disciplinas.stream()
                                        .map(d -> new DisciplinaDTO(
                                            d.getId(),
                                            d.getNome(),
                                            d.getDescricao()
                                        ))
                                        .toList();
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Disciplinas retornadas com sucesso!",
            new PageResponse<>(
                disciplinasDTO,
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
            var disciplina = disciplinaService.buscarPorId(id);
            var disciplinaDTO = new DisciplinaDTO(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao()
            );
            return ResponseEntity.status(HttpStatus.OK)
            .body(new AuthResponse<>("Disciplina encontrada com sucesso", disciplinaDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id, @Valid @RequestBody Disciplina disciplina) {
        try {
            disciplina.setId(id);
            var disciplinaAtualizada = disciplinaService.salvar(disciplina);
            return ResponseEntity.ok(new AuthResponse<>("Disciplina atualizada com sucesso!", disciplinaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            if (disciplinaService.buscarPorId(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Disciplina não encontrada com id: " + id));
            }
            disciplinaService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Disciplina deletada com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
