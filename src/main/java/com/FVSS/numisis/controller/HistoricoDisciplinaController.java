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

import com.FVSS.numisis.domain.model.HistoricoDisciplina;
import com.FVSS.numisis.dto.PageResponse;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.HistoricoDisciplinaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/historicos-disciplinas")
public class HistoricoDisciplinaController {

    private final HistoricoDisciplinaService historicoDisciplinaService;

    public HistoricoDisciplinaController(HistoricoDisciplinaService historicoDisciplinaService) {
        this.historicoDisciplinaService = historicoDisciplinaService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody HistoricoDisciplina historicoDisciplina) {
        try {
            var historicoSaved = historicoDisciplinaService.salvar(historicoDisciplina);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Histórico salvo com sucesso!", historicoSaved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar(Pageable pageable) {
        try {
            Page<HistoricoDisciplina> page = historicoDisciplinaService.listarTodos(pageable);
            List<HistoricoDisciplina> historicos = page.getContent()
                                     .stream()
                                     .toList();
            
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Históricos retornados com sucesso!",
            new PageResponse<>(
                historicos,
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

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<AuthResponse<?>> listarPorAluno(@PathVariable Long alunoId, Pageable pageable) {
        try {
            Page<HistoricoDisciplina> page = historicoDisciplinaService.listarPorAluno(alunoId, pageable);
            List<HistoricoDisciplina> historicos = page.getContent()
                                     .stream()
                                     .toList();

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Históricos do aluno retornados com sucesso!",
            new PageResponse<>(
                historicos,
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

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<AuthResponse<?>> listarPorTurma(@PathVariable Long turmaId, Pageable pageable) {
        try {
            Page<HistoricoDisciplina> page = historicoDisciplinaService.listarPorTurma(turmaId, pageable);
            List<HistoricoDisciplina> historicos = page.getContent()
                                     .stream()
                                     .toList();

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Históricos da turma retornados com sucesso!",
            new PageResponse<>(
                historicos,
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
            var historico = historicoDisciplinaService.buscarPorId(id);
            if (historico == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new AuthResponse<>("Histórico encontrado com sucesso!", historico));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id,
            @Valid @RequestBody HistoricoDisciplina historicoDisciplina) {
        try {
            historicoDisciplina.setId(id);
            return ResponseEntity.ok(new AuthResponse<>("Histórico atualizado com sucesso!", historicoDisciplinaService.salvar(historicoDisciplina)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            historicoDisciplinaService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Histórico deletado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
