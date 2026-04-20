package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Pessoa;
import com.FVSS.numisis.infrastructure.repository.PessoaRepository;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> listarTodos() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        pessoaRepository.deleteById(id);
    }
}
