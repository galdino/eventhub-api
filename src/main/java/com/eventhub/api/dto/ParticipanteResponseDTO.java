package com.eventhub.api.dto;

import com.eventhub.api.model.Participante;

public record ParticipanteResponseDTO(
    Long id,
    String nome,
    String email
) {
    public static ParticipanteResponseDTO gerarDTO(Participante participante) {
        return new ParticipanteResponseDTO(
            participante.getId(),
            participante.getNome(),
            participante.getEmail()
        );
    }
}
