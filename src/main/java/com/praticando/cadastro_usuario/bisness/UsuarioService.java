package com.praticando.cadastro_usuario.bisness;

import com.praticando.cadastro_usuario.bisness.exceptions.EmailNaoEncontradoException;
import com.praticando.cadastro_usuario.bisness.exceptions.IdUsuarioNaoEncontradoException;
import com.praticando.cadastro_usuario.infractructure.dto.UsuarioDTO;
import com.praticando.cadastro_usuario.infractructure.entity.Usuario;
import com.praticando.cadastro_usuario.infractructure.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;

    }

    public void salvarUsuario(UsuarioDTO usuario) {
        Usuario user = UsuarioDTO.paraEntidade(usuario);
        repository.save(user);
        log.info("Usuario {} salvo com sucesso", usuario.nome());
    }

    public List<UsuarioDTO> buscarTodosUsuarios(){
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

    public void atualizarUsuario(String id, UsuarioDTO usuario) {
        Usuario usuarioExistente = repository.findById(id).orElseThrow(
                () -> new IdUsuarioNaoEncontradoException(id));

        Usuario usuarioAtualizado = Usuario.builder()
                .email(usuario.email() != null ? usuario.email() : usuarioExistente.getEmail())
                .nome(usuario.nome() != null ? usuario.nome() : usuarioExistente.getNome())
                .id(usuarioExistente.getId())
                .build();
        repository.save(usuarioAtualizado);
    }

    public void deletarUsuario(String id) {
        UsuarioDTO usuario = buscarPorId(id);
        if (usuario != null) {
            repository.deleteById(id);
            log.info("Usuario {} removido com sucesso", usuario.nome());
        }

    }


}
