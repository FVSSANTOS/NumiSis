package com.FVSS.numisis.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import com.FVSS.numisis.response.AuthResponse;

import com.FVSS.numisis.service.ArquivoService;

@RestController
@RequestMapping("/api/arquivos")
public class ArquivoController {
    
    private final ArquivoService arquivoService;

    @Autowired
    public ArquivoController(ArquivoService arquivoService) {
        this.arquivoService = arquivoService;
    }

    @GetMapping("/files")
    public ResponseEntity<AuthResponse<?>> listFiles() {
        try {
            var files = arquivoService.loadAll()
                    .map(path -> MvcUriComponentsBuilder
                    .fromMethodName(
                        ArquivoController.class,
                        "serveFile",
                        path.getFileName().toString()
                    )
                    .build()
                    .toUri()
                    .toString()
                    )
                    .toList();
            return ResponseEntity.ok(new AuthResponse<>("Arquivos listados com sucesso!", files));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse<>("Falha ao listar arquivos.", e));
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource file = arquivoService.loadAsResource(filename);

            if (file == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFilename() + "\""
                    )
                    .body(file);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<AuthResponse<?>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            arquivoService.store(file);
            return ResponseEntity.ok(new AuthResponse<>("Arquivo enviado com sucesso!", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse<>("Falha ao enviar arquivo.", e));
        }
    }

    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<AuthResponse<?>> deleteFile(@PathVariable String filename) {
        try {
            arquivoService.delete(filename);
            return ResponseEntity.ok(new AuthResponse<>("Arquivo excluído com sucesso!", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse<>("Falha ao excluir arquivo.", e));
        }
    }
}
