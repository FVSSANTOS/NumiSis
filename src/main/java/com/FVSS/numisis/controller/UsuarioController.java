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

import com.FVSS.numisis.domain.model.Usuario;
import com.FVSS.numisis.exception.exceptions.RegraNegocioException;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody Usuario usuario) {
        try {
            var usuarioSalvo = usuarioService.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse<>("Usuário salvo com sucesso!", usuarioSalvo));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar() {
        try {
            return ResponseEntity.ok(new AuthResponse<>("Usuários retornados com sucesso!", usuarioService.listarTodos()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> buscar(@PathVariable Long id) {
        try {
           var usuario = usuarioService.buscarPorId(id);
           return ResponseEntity.status(HttpStatus.OK)
                   .body(new AuthResponse<>("Usuário encontrado com sucesso!", usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        try {
            usuario.setId(id);
            var usuarioAtualizado = usuarioService.salvar(usuario);
            return ResponseEntity.ok(new AuthResponse<>("Usuário atualizado com sucesso!", usuarioAtualizado));
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
            if (usuarioService.buscarPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse<>("Usuário não encontrado com id: " + id));
            }
            usuarioService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new AuthResponse<>("Usuário deletado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse<>("Erro no processamento do servidor", e));
        }
    }
}
