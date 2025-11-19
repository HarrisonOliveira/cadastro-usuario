package com.praticando.cadastro_usuario.bisness.service;

import com.praticando.cadastro_usuario.bisness.exceptions.EmailNaoEncontradoException;
import com.praticando.cadastro_usuario.bisness.exceptions.IdUsuarioNaoEncontradoException;
import com.praticando.cadastro_usuario.infractructure.dto.UsuarioDTO;
import com.praticando.cadastro_usuario.infractructure.entity.Usuario;
import com.praticando.cadastro_usuario.infractructure.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsuarioService {

    UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;

    }

    public Usuario criaUsuario(UsuarioDTO usuario) {
        Usuario user = UsuarioDTO.paraEntidade(usuario);
        repository.save(user);
        log.info("Usuario {} criado com sucesso", usuario.nome());
        return user;
    }

    public List<UsuarioDTO> buscarTodosUsuarios() {
        return repository.findAll()
                .stream()
                .map(UsuarioDTO::paraDTO)
                .toList();
    }

    public UsuarioDTO buscarPorId(String id) {
        Usuario usuario = repository.findById(id).orElseThrow(
                () -> new IdUsuarioNaoEncontradoException(id));
        return UsuarioDTO.paraDTO(usuario);
    }

    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = repository.findByEmail(email).orElseThrow(
                () -> new EmailNaoEncontradoException(email));
        return UsuarioDTO.paraDTO(usuario);
    }

    public UsuarioDTO atualizarUsuario(String id, UsuarioDTO usuario) {
        Usuario usuarioExistente = repository.findById(id).orElseThrow(
                () -> new IdUsuarioNaoEncontradoException(id));

        Usuario usuarioAtualizado = Usuario.builder()
                .id(usuarioExistente.getId())
                .nome(usuario.nome() != null ? usuario.nome() : usuarioExistente.getNome())
                .email(usuario.email() != null ? usuario.email() : usuarioExistente.getEmail())
                .build();

        repository.save(usuarioAtualizado);
        log.info("Informações do usuário {} foram atualizadas com sucesso", usuarioAtualizado.getNome());

        return UsuarioDTO.paraDTO(usuarioAtualizado);
    }

    public void deletarUsuario(String id) {
        Optional<Usuario> usuario = repository.findById(id);

        if (usuario.isPresent()) {
            repository.deleteById(id);
            log.info("Usuario {} removido com sucesso", usuario.get().getNome());
        } else throw new IdUsuarioNaoEncontradoException("Não foi possível encontrar o usuario com o {} informado");

    }


}
