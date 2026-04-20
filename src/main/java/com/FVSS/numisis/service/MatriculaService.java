package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Matricula;
import com.FVSS.numisis.infrastructure.repository.MatriculaRepository;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    public Matricula salvar(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public List<Matricula> listarTodos() {
        return matriculaRepository.findAll();
    }

    public Optional<Matricula> buscarPorId(Long id) {
        return matriculaRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        matriculaRepository.deleteById(id);
    }
}
