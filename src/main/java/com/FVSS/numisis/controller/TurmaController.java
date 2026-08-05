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

import com.FVSS.numisis.domain.model.Turma;
import com.FVSS.numisis.dto.PageResponse;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.TurmaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody Turma turma) {
        try {
            var turmaSalva = turmaService.salvar(turma);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Turma salva com sucesso!", turmaSalva));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar(Pageable pageable) {
        try {
            Page<Turma> page = turmaService.listarTodos(pageable);
            List<Turma> turmas = page.getContent()
                                     .stream()
                                     .toList();

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Turmas retornadas com sucesso!",
            new PageResponse<>(
                turmas,
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
            var turma = turmaService.buscarPorId(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AuthResponse<>("Turma encontrada com sucesso!", turma));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id, @Valid @RequestBody Turma turma) {
        try {
            turma.setId(id);
            var turmaAtualizada = turmaService.salvar(turma);
            return ResponseEntity.ok(new AuthResponse<>("Turma atualizada com sucesso!", turmaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            if (turmaService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Turma não encontrada com id: " + id));
            }
            turmaService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Turma deletada com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
