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

import com.FVSS.numisis.domain.model.CursoDisciplina;
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
    public ResponseEntity<CursoDisciplina> criar(@Valid @RequestBody CursoDisciplina cursoDisciplina) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDisciplinaService.salvar(cursoDisciplina));
    }

    @GetMapping
    public ResponseEntity<List<CursoDisciplina>> listar() {
        return ResponseEntity.ok(cursoDisciplinaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDisciplina> buscar(@PathVariable Long id) {
        return cursoDisciplinaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDisciplina> atualizar(@PathVariable Long id,
            @Valid @RequestBody CursoDisciplina cursoDisciplina) {
        cursoDisciplina.setId(id);
        return ResponseEntity.ok(cursoDisciplinaService.salvar(cursoDisciplina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (cursoDisciplinaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        cursoDisciplinaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
