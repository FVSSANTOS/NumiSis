package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Telefone;
import com.FVSS.numisis.infrastructure.repository.TelefoneRepository;

@Service
public class TelefoneService {

    private final TelefoneRepository telefoneRepository;

    public TelefoneService(TelefoneRepository telefoneRepository) {
        this.telefoneRepository = telefoneRepository;
    }

    public Telefone salvar(Telefone telefone) {
        return telefoneRepository.save(telefone);
    }

    public List<Telefone> listarTodos() {
        return telefoneRepository.findAll();
    }

    public Optional<Telefone> buscarPorId(Long id) {
        return telefoneRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        telefoneRepository.deleteById(id);
    }
}
