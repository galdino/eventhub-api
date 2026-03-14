package com.eventhub.api.dto;

import com.eventhub.api.model.Evento;
import java.time.LocalDateTime;

public record EventoResponseDTO(
    Long id,
    String nome,
    LocalDateTime data,
    String local,
    Integer capacidade
) {
    public static EventoResponseDTO gerarDTO(Evento evento) {
        return new EventoResponseDTO(
            evento.getId(),
            evento.getNome(),
            evento.getData(),
            evento.getLocal(),
            evento.getCapacidade()
        );
    }
}
