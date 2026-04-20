package com.FVSS.numisis.domain.model;

import com.FVSS.numisis.domain.enums.StatusHistorico;

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

	@Column(name = "media")
	private Double media;

	@Column(name = "faltas")
	private Integer faltas;

	@Column(name = "ano")
	private Integer ano;

	@Column(name = "semestre")
	private Integer semestre;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno_id")
	private Aluno aluno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "turma_id")
	private Turma turma;

	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	private StatusHistorico situacao;

	public HistoricoDisciplina() {
	}

}
