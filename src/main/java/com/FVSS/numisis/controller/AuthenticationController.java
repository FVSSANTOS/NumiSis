package com.FVSS.numisis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FVSS.numisis.dto.JwtResponse;
import com.FVSS.numisis.dto.LoginRequest;
import com.FVSS.numisis.infrastructure.security.UserDetailsImpl;
import com.FVSS.numisis.response.AuthResponse;
import com.FVSS.numisis.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.login(), request.senha()));

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String token = jwtService.generateToken(userDetails);

		JwtResponse jwtResponse = new JwtResponse(
				token,
				userDetails.getUsuario().getId(),
				userDetails.getUsername(),
				userDetails.getUsuario().getRole());

		return ResponseEntity.ok(new AuthResponse<>("Login realizado com sucesso", jwtResponse));
	}
}
