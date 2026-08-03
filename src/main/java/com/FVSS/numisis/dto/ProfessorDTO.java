package com.FVSS.numisis.dto;


public record ProfessorDTO(
    long id,
    String nome,
    String cpf,
    int idade,
    String cargaHoraria
) {}
