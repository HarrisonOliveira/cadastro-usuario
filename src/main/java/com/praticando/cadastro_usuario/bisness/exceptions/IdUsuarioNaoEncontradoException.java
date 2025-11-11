package com.praticando.cadastro_usuario.bisness.exceptions;

public class IdUsuarioNaoEncontradoException extends RuntimeException{
    public IdUsuarioNaoEncontradoException(String ID){
        super("Usuário com o ID " + ID + " não foi encontrado ou não pertence a um usuário");
    }
}
