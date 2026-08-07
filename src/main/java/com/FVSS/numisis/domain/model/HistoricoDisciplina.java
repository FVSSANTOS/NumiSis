package com.FVSS.numisis.domain.model;

import com.FVSS.numisis.domain.enums.StatusHistorico;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "historico_disciplina")
@Getter
@Setter
public class HistoricoDisciplina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nota")
	private Double nota;

	@Column(name = "faltas")
	private Integer faltas;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno_id")
	private Aluno aluno;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "turma_id")
	private Turma turma;

	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	private StatusHistorico situacao;

	public HistoricoDisciplina() {
	}

	@Transient
	public String getDisciplina() {
		return turma != null ? turma.getDisciplinaNome() : null;
	}

	@Transient
	public Long getAlunoId() {
		return aluno != null ? aluno.getId() : null;
	}

	@Transient
	public String getAlunoNome() {
		return aluno != null ? aluno.getNome() : null;
	}

	// ano/semestre não são coluna própria: são os mesmos da turma (uma turma já representa um ano+semestre específico).
	@Transient
	public Integer getAno() {
		return turma != null ? turma.getAno() : null;
	}

	@Transient
	public Integer getSemestre() {
		return turma != null ? turma.getSemestre() : null;
	}

}
