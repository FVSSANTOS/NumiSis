package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Aluno;
import com.FVSS.numisis.exception.exceptions.NaoEncontradoException;
import com.FVSS.numisis.exception.exceptions.RegraNegocioException;
import com.FVSS.numisis.infrastructure.repository.AlunoRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno salvar(Aluno aluno) {

        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new RegraNegocioException("Já existe um aluno com esse CPF.");
        }
        
        if(aluno.getIdade() < 18 &&
        (aluno.getNomeMae() == null || aluno.getNomePai() == null)){

            throw new RegraNegocioException(
                "Para menores de idade, nome do pai e da mãe são obrigatórios."
            );
           }
        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(Aluno aluno){
        buscarPorId(aluno.getId());
        return alunoRepository.save(aluno);
    }

    public  Page<Aluno> listarTodos(Pageable pageable) {
        return alunoRepository.findAll(pageable);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
            .orElseThrow(() -> new NaoEncontradoException("Aluno não encontrado com id: " + id));
    }

    public void deletarPorId(Long id) {
        buscarPorId(id);
        alunoRepository.deleteById(id);
    }
}
