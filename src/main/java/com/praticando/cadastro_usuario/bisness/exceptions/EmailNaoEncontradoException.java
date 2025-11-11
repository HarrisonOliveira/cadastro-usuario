package com.praticando.cadastro_usuario.bisness.exceptions;

public class EmailNaoEncontradoException extends RuntimeException{


    public EmailNaoEncontradoException(String Email){
        super("Usuário com o Email: " + Email + " não foi encontrado ou não pertence a um usuário");
    }
}
