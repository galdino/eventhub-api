package com.eventhub.api.exception;

public class ParticipanteNaoEncontradoException extends RuntimeException {
    public ParticipanteNaoEncontradoException(Long id) {
        super(String.format("Participante com id %d não encontrado", id));
    }
}
