package com.FVSS.numisis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.enums.Role;
import com.FVSS.numisis.domain.model.Turma;
import com.FVSS.numisis.domain.model.Usuario;
import com.FVSS.numisis.exception.exceptions.NaoEncontradoException;
import com.FVSS.numisis.infrastructure.repository.AlunoRepository;
import com.FVSS.numisis.infrastructure.repository.ProfessorRepository;
import com.FVSS.numisis.infrastructure.repository.TurmaRepository;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;

    public TurmaService(TurmaRepository turmaRepository, ProfessorRepository professorRepository,
            AlunoRepository alunoRepository) {
        this.turmaRepository = turmaRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
    }

    public Turma salvar(Turma turma) {
        return turmaRepository.save(turma);
    }

    public Page<Turma> listarTodos(Pageable pageable) {
        return turmaRepository.findAll(pageable);
    }

    public Page<Turma> listarPorPessoa(Long pessoaId, Pageable pageable) {
        return turmaRepository.findByPessoaId(pessoaId, pageable);
    }


    public Page<Turma> listarDoUsuarioLogado(Usuario usuario, Pageable pageable) {
        Long pessoaId;

        if (usuario.getRole() == Role.PROFESSOR) {
            pessoaId = professorRepository.findByUsuarioId(usuario.getId())
                    .orElseThrow(() -> new NaoEncontradoException("Professor não encontrado para o usuário logado"))
                    .getId();
        } else if (usuario.getRole() == Role.ALUNO) {
            pessoaId = alunoRepository.findByUsuarioId(usuario.getId())
                    .orElseThrow(() -> new NaoEncontradoException("Aluno não encontrado para o usuário logado"))
                    .getId();
        } else {
            throw new NaoEncontradoException("Usuário logado não possui turmas vinculadas.");
        }

        return turmaRepository.findByPessoaId(pessoaId, pageable);
    }

    public Optional<Turma> buscarPorId(Long id) {
        return turmaRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        turmaRepository.deleteById(id);
    }
}
