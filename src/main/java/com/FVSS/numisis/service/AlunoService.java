package com.FVSS.numisis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.domain.model.Aluno;
import com.FVSS.numisis.domain.model.Usuario;
import com.FVSS.numisis.exception.exceptions.NaoEncontradoException;
import com.FVSS.numisis.exception.exceptions.RegraNegocioException;
import com.FVSS.numisis.infrastructure.repository.AlunoRepository;
import com.FVSS.numisis.infrastructure.repository.PessoaRepository;
import com.FVSS.numisis.infrastructure.repository.UsuarioRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository alunoRepository, UsuarioRepository usuarioRepository,
            PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Aluno salvar(Aluno aluno) {

        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new RegraNegocioException("Já existe um aluno com esse CPF.");
        }

        if (aluno.getUsuario() != null && aluno.getUsuario().getId() == null
                && usuarioRepository.existsByLogin(aluno.getUsuario().getLogin())) {
            throw new RegraNegocioException("Já existe um usuário com esse login.");
        }

        if (aluno.getUsuario() != null && aluno.getUsuario().getId() != null
                && pessoaRepository.existsByUsuarioId(aluno.getUsuario().getId())) {
            throw new RegraNegocioException("Esse usuário já está vinculado a outra pessoa.");
        }

        if(aluno.getIdade() < 18 &&
        (aluno.getNomeMae() == null || aluno.getNomePai() == null)){

            throw new RegraNegocioException(
                "Para menores de idade, nome do pai e da mãe são obrigatórios."
            );
           }

        tratarSenhaUsuario(aluno.getUsuario());

        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(Aluno aluno){
        Aluno existente = buscarPorId(aluno.getId());

        if (aluno.getUsuario() == null) {
            aluno.setUsuario(existente.getUsuario());
        } else if (aluno.getUsuario().getId() != null
                && pessoaRepository.existsByUsuarioIdAndIdNot(aluno.getUsuario().getId(), aluno.getId())) {
            throw new RegraNegocioException("Esse usuário já está vinculado a outra pessoa.");
        }

        tratarSenhaUsuario(aluno.getUsuario());

        return alunoRepository.save(aluno);
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

    public Page<Aluno> listarTodos(Pageable pageable) {
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
