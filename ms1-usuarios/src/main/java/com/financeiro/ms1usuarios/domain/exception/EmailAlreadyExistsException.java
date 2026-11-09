package com.financeiro.ms1usuarios.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email){
        super("Já existe um registro com esse email: "+ email);
    }
}
