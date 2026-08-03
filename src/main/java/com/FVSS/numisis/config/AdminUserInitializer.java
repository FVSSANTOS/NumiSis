package com.FVSS.numisis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.FVSS.numisis.domain.enums.Role;
import com.FVSS.numisis.domain.model.Usuario;
import com.FVSS.numisis.infrastructure.repository.UsuarioRepository;
import com.FVSS.numisis.service.UsuarioService;

@Component
public class AdminUserInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);

	private static final String ADMIN_LOGIN = "admin";
	private static final String ADMIN_SENHA = "admin123";

	private final UsuarioRepository usuarioRepository;
	private final UsuarioService usuarioService;

	public AdminUserInitializer(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioService = usuarioService;
	}

	@Override
	public void run(String... args) {
		if (usuarioRepository.existsByLogin(ADMIN_LOGIN)) {
			return;
		}

		Usuario admin = new Usuario();
		admin.setLogin(ADMIN_LOGIN);
		admin.setSenha(ADMIN_SENHA);
		admin.setRole(Role.ADMIN);

		usuarioService.salvar(admin);
		log.info("Usuário administrador padrão criado (login: {})", ADMIN_LOGIN);
	}
}
