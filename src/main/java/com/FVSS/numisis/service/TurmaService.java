package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Turma;
import com.FVSS.numisis.infrastructure.repository.TurmaRepository;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public Turma salvar(Turma turma) {
        return turmaRepository.save(turma);
    }

    public Page<Turma> listarTodos(Pageable pageable) {
        return turmaRepository.findAll(pageable);
    }

    public Optional<Turma> buscarPorId(Long id) {
        return turmaRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        turmaRepository.deleteById(id);
    }
}
