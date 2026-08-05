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

import com.FVSS.numisis.domain.model.Endereco;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.EnderecoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody Endereco endereco) {
        try {
            var enderecoSalvo = enderecoService.salvar(endereco);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Endereço salvo com sucesso!", enderecoSalvo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar() {
        try {
            return ResponseEntity.ok(new AuthResponse<>("Endereços retornados com sucesso!", enderecoService.listarTodos()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> buscar(@PathVariable Long id) {
        try {
            var endereco = enderecoService.buscarPorId(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AuthResponse<>("Endereço encontrado com sucesso!", endereco));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id, @Valid @RequestBody Endereco endereco) {
        try {
            endereco.setId(id);
            var enderecoAtualizado = enderecoService.salvar(endereco);
            return ResponseEntity.ok(new AuthResponse<>("Endereço atualizado com sucesso!", enderecoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            if (enderecoService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Endereço não encontrado com id: " + id));
            }
            enderecoService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Endereço deletado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
