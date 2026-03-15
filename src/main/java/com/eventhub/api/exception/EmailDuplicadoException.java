package com.eventhub.api.exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String email) {
        super(String.format("Participante com e-mail '%s' já cadastrado", email));
    }
}
