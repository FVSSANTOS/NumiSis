package com.FVSS.numisis.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "professor")
@Getter
@Setter
public class Professor extends Pessoa{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(name = "carga_horaria")
	private String cargaHoraria;

	@OneToOne
	@JoinColumn(name = "dados_bancarios_id")
	private DadosBancarios dadosBancarios;

	@OneToMany(mappedBy = "professor")
	private List<Turma> turmas = new ArrayList<>();

	public Professor() {
	}

}
