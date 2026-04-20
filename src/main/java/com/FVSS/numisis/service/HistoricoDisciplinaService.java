package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.HistoricoDisciplina;
import com.FVSS.numisis.infrastructure.repository.HistoricoDisciplinaRepository;

@Service
public class HistoricoDisciplinaService {

    private final HistoricoDisciplinaRepository historicoDisciplinaRepository;

    public HistoricoDisciplinaService(HistoricoDisciplinaRepository historicoDisciplinaRepository) {
        this.historicoDisciplinaRepository = historicoDisciplinaRepository;
    }

    public HistoricoDisciplina salvar(HistoricoDisciplina historicoDisciplina) {
        return historicoDisciplinaRepository.save(historicoDisciplina);
    }

    public List<HistoricoDisciplina> listarTodos() {
        return historicoDisciplinaRepository.findAll();
    }

    public Optional<HistoricoDisciplina> buscarPorId(Long id) {
        return historicoDisciplinaRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        historicoDisciplinaRepository.deleteById(id);
    }
}
