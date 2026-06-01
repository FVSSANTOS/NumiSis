package com.FVSS.numisis.dto;

import com.FVSS.numisis.domain.enums.Role;

public record UsuarioDTO(
    long id,
    String login,
    Role role
) {}
