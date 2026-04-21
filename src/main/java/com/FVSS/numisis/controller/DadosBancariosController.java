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

import com.FVSS.numisis.domain.model.DadosBancarios;
import com.FVSS.numisis.service.DadosBancariosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/dados-bancarios")
public class DadosBancariosController {

    private final DadosBancariosService dadosBancariosService;

    public DadosBancariosController(DadosBancariosService dadosBancariosService) {
        this.dadosBancariosService = dadosBancariosService;
    }

    @PostMapping
    public ResponseEntity<DadosBancarios> criar(@Valid @RequestBody DadosBancarios dadosBancarios) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(dadosBancariosService.salvar(dadosBancarios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DadosBancarios>> listar() {
        try {
            return ResponseEntity.ok(dadosBancariosService.listarTodos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosBancarios> buscar(@PathVariable Long id) {
        try {
            return dadosBancariosService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosBancarios> atualizar(@PathVariable Long id,
            @Valid @RequestBody DadosBancarios dadosBancarios) {
        try {
            dadosBancarios.setId(id);
            return ResponseEntity.ok(dadosBancariosService.salvar(dadosBancarios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            if (dadosBancariosService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            dadosBancariosService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
