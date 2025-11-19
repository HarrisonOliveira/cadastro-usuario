package com.praticando.cadastro_usuario.controller;

import com.praticando.cadastro_usuario.bisness.service.UsuarioService;
import com.praticando.cadastro_usuario.infractructure.dto.UsuarioDTO;
import com.praticando.cadastro_usuario.infractructure.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value ="/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@Validated @RequestBody UsuarioDTO usuario){
        Usuario usuarioSalva =  service.criaUsuario(usuario);
        UsuarioDTO usuarioDTO = UsuarioDTO.paraDTO(usuarioSalva);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(usuarioSalva.getId())
                .toUri();

        return ResponseEntity.created(uri).body(usuarioDTO);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> buscarTodosUsuarios(){
        return ResponseEntity.ok(service.buscarTodosUsuarios());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable("id") String id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") String id){
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable("id") String id, @RequestBody UsuarioDTO usuario){
        UsuarioDTO usuarioAtualizado = service.atualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

}
