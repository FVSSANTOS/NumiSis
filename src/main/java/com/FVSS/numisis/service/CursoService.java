package com.FVSS.numisis.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Curso;
import com.FVSS.numisis.infrastructure.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Curso salvar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Page<Curso> listarTodos(Pageable pageable) {
        return cursoRepository.findAll(pageable);
    }

    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        cursoRepository.deleteById(id);
    }
}
