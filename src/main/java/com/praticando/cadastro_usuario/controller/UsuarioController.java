package com.praticando.cadastro_usuario.controller;

import com.praticando.cadastro_usuario.bisness.service.UsuarioService;
import com.praticando.cadastro_usuario.infractructure.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;


    @PostMapping(value = "/cadastrarUsuario")
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO usuario){
        service.salvarUsuario(usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<UsuarioDTO>> buscarTodosUsuarios(){
        return ResponseEntity.ok(service.buscarTodosUsuarios());
    }

    @GetMapping(value = "/getById/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable("id") String id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping(value = "/getByEmail/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") String id){
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable("id") String id, @RequestBody UsuarioDTO usuario){
        service.atualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuario);
    }


}
