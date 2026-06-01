package com.FVSS.numisis.dto;

public record TurmaDTO(
    long id,
    int ano,
    int semestre,
    String sala,
    String horarioInicio,
    String horarioTermino,
    long disciplinaId,
    long professorId
){}
