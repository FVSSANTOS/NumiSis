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

import com.FVSS.numisis.domain.model.Telefone;
import com.FVSS.numisis.service.TelefoneService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/telefones")
public class TelefoneController {

    private final TelefoneService telefoneService;

    public TelefoneController(TelefoneService telefoneService) {
        this.telefoneService = telefoneService;
    }

    @PostMapping
    public ResponseEntity<Telefone> criar(@Valid @RequestBody Telefone telefone) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(telefoneService.salvar(telefone));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Telefone>> listar() {
        try {
            return ResponseEntity.ok(telefoneService.listarTodos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telefone> buscar(@PathVariable Long id) {
        try {
            return telefoneService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Telefone> atualizar(@PathVariable Long id, @Valid @RequestBody Telefone telefone) {
        try {
            telefone.setId(id);
            return ResponseEntity.ok(telefoneService.salvar(telefone));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            if (telefoneService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            telefoneService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
