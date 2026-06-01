package com.FVSS.numisis.dto;

import com.FVSS.numisis.domain.enums.Role;

public record JwtResponse(
		String token,
		String tipo,
		Long id,
		String login,
		Role role) {

	public JwtResponse(String token, Long id, String login, Role role) {
		this(token, "Bearer", id, login, role);
	}
}
