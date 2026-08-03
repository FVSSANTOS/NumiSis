package com.FVSS.numisis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Professor;
import com.FVSS.numisis.exception.exceptions.NaoEncontradoException;
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

    public Professor atualizar(Professor professor) {
        buscarPorId(professor.getId());
        return professorRepository.save(professor);
    }
    public Page<Professor> listarTodos(Pageable pageable) {
        return professorRepository.findAll(pageable);
    }

    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id)
         .orElseThrow(() -> new NaoEncontradoException("Professor não encontrado com id: " + id));
    }

    public void deletarPorId(Long id) {
        professorRepository.deleteById(id);
    }
}
