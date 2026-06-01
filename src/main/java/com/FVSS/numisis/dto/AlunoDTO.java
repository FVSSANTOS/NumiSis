package com.FVSS.numisis.dto;

public record AlunoDTO<T>(
     long id,
     String nome,
     String cpf,
     int idade
 ) {}
