package com.FVSS.numisis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Disciplina;
import com.FVSS.numisis.exception.exceptions.NaoEncontradoException;
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

    public Disciplina buscarPorId(Long id) {
        return disciplinaRepository.findById(id)
            .orElseThrow(() -> new NaoEncontradoException("Disciplina não encontrada com id: " + id));
    }

    public void deletarPorId(Long id) {
        disciplinaRepository.deleteById(id);
    }
}
