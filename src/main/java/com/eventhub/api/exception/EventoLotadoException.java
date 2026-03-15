package com.eventhub.api.exception;

public class EventoLotadoException extends RuntimeException {
    public EventoLotadoException(Long eventoId) {
        super(String.format("Evento com id %d está lotado - capacidade esgotada", eventoId));
    }
}
