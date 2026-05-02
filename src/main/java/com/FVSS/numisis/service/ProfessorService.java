package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Professor;
import com.FVSS.numisis.infrastructure.repository.ProfessorRepository;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Professor salvar(Professor professor) {
        return professorRepository.save(professor);
    }

    public Page<Professor> listarTodos(Pageable pageable) {
        return professorRepository.findAll(pageable);
    }

    public Optional<Professor> buscarPorId(Long id) {
        return professorRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        professorRepository.deleteById(id);
    }
}
