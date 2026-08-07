package com.FVSS.numisis.domain.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "matricula")
@Getter
@Setter
public class Matricula {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_matricula")
	private LocalDate dataMatricula;

	@Column(name = "situacao")
	private String situacao;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno_id")
	private Aluno aluno;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id")
	private Curso curso;

	public Matricula() {
	}

	// Ids/nomes derivados, sem desfazer o write-only de `aluno`/`curso` acima.
	@Transient
	public Long getAlunoId() {
		return aluno != null ? aluno.getId() : null;
	}

	@Transient
	public Long getCursoId() {
		return curso != null ? curso.getId() : null;
	}

	@Transient
	public String getAlunoNome() {
		return aluno != null ? aluno.getNome() : null;
	}

	@Transient
	public String getCursoNome() {
		return curso != null ? curso.getNome() : null;
	}

}
