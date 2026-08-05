package com.FVSS.numisis.dto;

import java.time.LocalDate;
import java.util.List;

import com.FVSS.numisis.domain.model.DadosBancarios;
import com.FVSS.numisis.domain.model.Endereco;
import com.FVSS.numisis.domain.model.Telefone;
import com.FVSS.numisis.domain.model.Turma;

public record ProfessorDTO(
    long id,
    String nome,
    String cpf,
    int idade,
    LocalDate dataNascimento,
    String email,
    String cargaHoraria,
    Endereco endereco,
    List<Telefone> telefones,
    DadosBancarios dadosBancarios,
    List<Turma> turmas,
    String usuarioLogin
) {}
