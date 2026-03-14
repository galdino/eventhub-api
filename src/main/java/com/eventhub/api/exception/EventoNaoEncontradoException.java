package com.eventhub.api.exception;

public class EventoNaoEncontradoException extends RuntimeException {
    public EventoNaoEncontradoException(Long id) {
        super(String.format("Evento com id %d não encontrado", id));
    }
}
