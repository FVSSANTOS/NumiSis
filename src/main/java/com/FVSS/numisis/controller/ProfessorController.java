package com.FVSS.numisis.controller;

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

import com.FVSS.numisis.domain.model.Professor;
import com.FVSS.numisis.dto.PageResponse;
import com.FVSS.numisis.exception.exceptions.RegraNegocioException;
import com.FVSS.numisis.mapper.ProfessorMapper;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.ProfessorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody Professor professor) {
        try {
            var professorSaved = professorService.salvar(professor);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse<>("Professor salvo com sucesso!", professorSaved));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new AuthResponse<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar(Pageable pageable) {
        try {
            Page<Professor> page = professorService.listarTodos(pageable);
            var professoresDTO = page.getContent()
                                     .stream()
                                     .map(ProfessorMapper::toDTO)
                                     .toList();

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Professores retornados com sucesso!",
            new PageResponse<>(
                professoresDTO,
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
            var professor = professorService.buscarPorId(id);
            var professorDTO = ProfessorMapper.toDTO(professor);
            return ResponseEntity.status(HttpStatus.OK)
            .body(new AuthResponse<>("Professor encontrado com sucesso", professorDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id, @Valid @RequestBody Professor professor) {
        try {
            professor.setId(id);
            var professorAtualizado = professorService.salvar(professor);
            return ResponseEntity.ok(new AuthResponse<>("Professor atualizado com sucesso!", professorAtualizado));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new AuthResponse<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            if (professorService.buscarPorId(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new AuthResponse<>("Professor não encontrado com id: " + id));
            }
            professorService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                 .body(new AuthResponse<>("Professor deletado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
