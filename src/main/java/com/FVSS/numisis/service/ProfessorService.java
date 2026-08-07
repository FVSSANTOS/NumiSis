package com.FVSS.numisis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Professor;
import com.FVSS.numisis.domain.model.Usuario;
import com.FVSS.numisis.exception.exceptions.NaoEncontradoException;
import com.FVSS.numisis.exception.exceptions.RegraNegocioException;
import com.FVSS.numisis.infrastructure.repository.PessoaRepository;
import com.FVSS.numisis.infrastructure.repository.ProfessorRepository;
import com.FVSS.numisis.infrastructure.repository.UsuarioRepository;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessorService(ProfessorRepository professorRepository, UsuarioRepository usuarioRepository,
            PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder) {
        this.professorRepository = professorRepository;
        this.usuarioRepository = usuarioRepository;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Professor salvar(Professor professor) {
        if (professor.getId() != null && professor.getUsuario() == null) {
            professor.setUsuario(buscarPorId(professor.getId()).getUsuario());
        }

        if (professor.getUsuario() != null && professor.getUsuario().getId() == null
                && usuarioRepository.existsByLogin(professor.getUsuario().getLogin())) {
            throw new RegraNegocioException("Já existe um usuário com esse login.");
        }

        if (professor.getUsuario() != null && professor.getUsuario().getId() != null) {
            boolean usuarioEmUso = professor.getId() == null
                    ? pessoaRepository.existsByUsuarioId(professor.getUsuario().getId())
                    : pessoaRepository.existsByUsuarioIdAndIdNot(professor.getUsuario().getId(), professor.getId());
            if (usuarioEmUso) {
                throw new RegraNegocioException("Esse usuário já está vinculado a outra pessoa.");
            }
        }

        tratarSenhaUsuario(professor.getUsuario());

        return professorRepository.save(professor);
    }

    // Evita salvar senha em texto puro ou zerar o hash quando o corpo não envia uma senha nova.
    private void tratarSenhaUsuario(Usuario usuario) {
        if (usuario == null) {
            return;
        }

        if (usuario.getId() == null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            return;
        }

        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            Usuario usuarioAtual = usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado com id: " + usuario.getId()));
            usuario.setSenha(usuarioAtual.getSenha());
        } else {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
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
