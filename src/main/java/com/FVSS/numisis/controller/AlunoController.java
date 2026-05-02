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

import com.FVSS.numisis.domain.model.Aluno;
import com.FVSS.numisis.dto.PageResponse;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.AlunoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse<?>> criar(@Valid @RequestBody Aluno aluno) {
        try {
            alunoService.salvar(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse<>("Aluno salvo com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(
                                     new AuthResponse<>("Erro no processamento do servidor", e)
                                  );
        }
    }

    @GetMapping
    public ResponseEntity<AuthResponse<?>> listar(Pageable pageable) {
        try {
            Page<Aluno> page = alunoService.listarTodos(pageable);
            List<Aluno> alunos = page.getContent()
                                     .stream()
                                     .toList();
            
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse<>(
            "Alunos retornados com sucesso!",
            new PageResponse<>(
                alunos,
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
        try{
            var aluno = alunoService.buscarPorId(id);
            return ResponseEntity.status(HttpStatus.OK)
            .body(new AuthResponse<>("Aluno encontrado com sucesso", aluno));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(
                                     new AuthResponse<>("Erro no processamento do servidor", e)
                                  );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> atualizar(@PathVariable Long id, @Valid @RequestBody Aluno aluno) {
        try {
            aluno.setId(id);
            var alunoAtualizado = alunoService.atualizar(aluno);
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new AuthResponse<>("Aluno atualizado com sucesso!", alunoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(
                                    new AuthResponse<>("Erro no processamento do servidor", e)
                                 );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<?>> remover(@PathVariable Long id) {
        try {
            alunoService.deletarPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                 .body(new AuthResponse<>("Aluno deletado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(
                                    new AuthResponse<>("Erro no processamento do servidor", e)
                                 );
        }
    }
}
