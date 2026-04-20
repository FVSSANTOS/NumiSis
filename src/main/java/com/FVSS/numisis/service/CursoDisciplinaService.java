package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.CursoDisciplina;
import com.FVSS.numisis.infrastructure.repository.CursoDisciplinaRepository;

@Service
public class CursoDisciplinaService {

    private final CursoDisciplinaRepository cursoDisciplinaRepository;

    public CursoDisciplinaService(CursoDisciplinaRepository cursoDisciplinaRepository) {
        this.cursoDisciplinaRepository = cursoDisciplinaRepository;
    }

    public CursoDisciplina salvar(CursoDisciplina cursoDisciplina) {
        return cursoDisciplinaRepository.save(cursoDisciplina);
    }

    public List<CursoDisciplina> listarTodos() {
        return cursoDisciplinaRepository.findAll();
    }

    public Optional<CursoDisciplina> buscarPorId(Long id) {
        return cursoDisciplinaRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        cursoDisciplinaRepository.deleteById(id);
    }
}
