package com.eventhub.api.dto;

import com.eventhub.api.model.Ingresso;
import java.time.LocalDateTime;

public record IngressoResponseDTO(
    Long id,
    Long eventoId,
    String nomeEvento,
    Long participanteId,
    String nomeParticipante,
    LocalDateTime dataCompra
) {
    public static IngressoResponseDTO gerarDTO(Ingresso ingresso) {
        return new IngressoResponseDTO(
            ingresso.getId(),
            ingresso.getEvento().getId(),
            ingresso.getEvento().getNome(),
            ingresso.getParticipante().getId(),
            ingresso.getParticipante().getNome(),
            ingresso.getDataCompra()
        );
    }
}
