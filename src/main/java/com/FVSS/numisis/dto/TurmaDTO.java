package com.FVSS.numisis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurmaDTO {
    private Long id;
    private Integer ano;
    private Integer semestre;
    private String sala;
    private String horarioInicio;
    private String horarioTermino;

    private Long disciplinaId;
    private Long professorId;
}
