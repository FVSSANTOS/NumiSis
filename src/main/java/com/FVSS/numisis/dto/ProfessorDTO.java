package com.FVSS.numisis.dto;

import com.FVSS.numisis.domain.model.DadosBancarios;


public record ProfessorDTO(
    long id,
    String nome,
    String cpf,
    int idade,
    String cargaHoraria,
    DadosBancarios dadosBancarios
) {}
