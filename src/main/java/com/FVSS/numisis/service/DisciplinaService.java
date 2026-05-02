package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Disciplina;
import com.FVSS.numisis.infrastructure.repository.DisciplinaRepository;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public Disciplina salvar(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    public Page<Disciplina> listarTodos(Pageable pageable) {
        return disciplinaRepository.findAll(pageable);
    }

    public Optional<Disciplina> buscarPorId(Long id) {
        return disciplinaRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        disciplinaRepository.deleteById(id);
    }
}
