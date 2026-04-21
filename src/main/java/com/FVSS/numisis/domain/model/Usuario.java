package com.FVSS.numisis.domain.model;

import com.FVSS.numisis.domain.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login")
	private String login;

	@Column(name = "senha")
	private String senha;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	public Usuario() {
	}

}
