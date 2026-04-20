package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.DadosBancarios;
import com.FVSS.numisis.infrastructure.repository.DadosBancariosRepository;

@Service
public class DadosBancariosService {

    private final DadosBancariosRepository dadosBancariosRepository;

    public DadosBancariosService(DadosBancariosRepository dadosBancariosRepository) {
        this.dadosBancariosRepository = dadosBancariosRepository;
    }

    public DadosBancarios salvar(DadosBancarios dadosBancarios) {
        return dadosBancariosRepository.save(dadosBancarios);
    }

    public List<DadosBancarios> listarTodos() {
        return dadosBancariosRepository.findAll();
    }

    public Optional<DadosBancarios> buscarPorId(Long id) {
        return dadosBancariosRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        dadosBancariosRepository.deleteById(id);
    }
}
