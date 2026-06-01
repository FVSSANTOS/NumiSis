package com.FVSS.numisis.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.FVSS.numisis.infrastructure.repository.UsuarioRepository;
import com.FVSS.numisis.infrastructure.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.findByLogin(username)
				.map(UserDetailsImpl::new)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Usuário não encontrado com o login: " + username));
	}
}
