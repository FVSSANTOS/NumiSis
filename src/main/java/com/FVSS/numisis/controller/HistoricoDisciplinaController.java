package com.FVSS.numisis.controller;

import java.util.List;

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
    public ResponseEntity<HistoricoDisciplina> criar(@Valid @RequestBody HistoricoDisciplina historicoDisciplina) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(historicoDisciplinaService.salvar(historicoDisciplina));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<HistoricoDisciplina>> listar() {
        try {
            return ResponseEntity.ok(historicoDisciplinaService.listarTodos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoDisciplina> buscar(@PathVariable Long id) {
        try {
            return historicoDisciplinaService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricoDisciplina> atualizar(@PathVariable Long id,
            @Valid @RequestBody HistoricoDisciplina historicoDisciplina) {
        try {
            historicoDisciplina.setId(id);
            return ResponseEntity.ok(historicoDisciplinaService.salvar(historicoDisciplina));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            if (historicoDisciplinaService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            historicoDisciplinaService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
