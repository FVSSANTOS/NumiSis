package com.FVSS.numisis.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.FVSS.numisis.infrastructure.repository.ArquivoRepository;

@Service
public class ArquivoService implements ArquivoRepository {

    private final Path rootLocation = Path.of("uploads"); 

    @Override
    public void store(MultipartFile file) {
        try {
            if(file.isEmpty()) {
                throw new RuntimeException("A imagem não pode ser nula.");
            }
            Path destinationFile = this.rootLocation.resolve(
                Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao salvar imagem.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao ler arquivos armazenados.", e);
        }
    }

    @Override
    public Path load(String filename) {
        Path file = this.rootLocation.resolve(filename).normalize();
        
        if(!file.startsWith(this.rootLocation.normalize())){
            throw new RuntimeException("Tentativa de acesso a arquivo fora do diretório não permitido.");
        }
        return this.rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new org.springframework.core.io.UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não foi possível ler o arquivo: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível ler o arquivo: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = load(filename);
            Files.deleteIfExists(file);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível deletar o arquivo: " + filename, e);
        }
    }
}
