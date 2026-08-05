package com.FVSS.numisis.controller;

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
import com.FVSS.numisis.response.AuthResponse;
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
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody DadosBancarios dadosBancarios) {
        try {
            var dadosSalvos = dadosBancariosService.salvar(dadosBancarios);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Dados bancários salvos com sucesso!", dadosSalvos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar() {
        try {
            return ResponseEntity.ok(new AuthResponse<>("Dados bancários retornados com sucesso!", dadosBancariosService.listarTodos()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> buscar(@PathVariable Long id) {
        try {
            var dados = dadosBancariosService.buscarPorId(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AuthResponse<>("Dados bancários encontrados com sucesso!", dados));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id,
            @Valid @RequestBody DadosBancarios dadosBancarios) {
        try {
            dadosBancarios.setId(id);
            var dadosAtualizados = dadosBancariosService.salvar(dadosBancarios);
            return ResponseEntity.ok(new AuthResponse<>("Dados bancários atualizados com sucesso!", dadosAtualizados));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            if (dadosBancariosService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Dados bancários não encontrados com id: " + id));
            }
            dadosBancariosService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Dados bancários deletados com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
