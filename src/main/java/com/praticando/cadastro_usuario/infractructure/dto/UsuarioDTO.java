package com.praticando.cadastro_usuario.infractructure.dto;

import com.praticando.cadastro_usuario.infractructure.entity.Usuario;
import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public record UsuarioDTO(String nome, String email) {



    public static UsuarioDTO paraDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    public static Usuario paraEntidade (UsuarioDTO usuarioDTO) {
        new Usuario();
        return  Usuario.builder()
                .nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .build();
    }
}
