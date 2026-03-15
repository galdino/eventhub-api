package com.eventhub.api.dto;

import jakarta.validation.constraints.NotNull;

public record IngressoRequestDTO(
    @NotNull(message = "O ID do evento é obrigatório")
    Long eventoId,

    @NotNull(message = "O ID do participante é obrigatório")
    Long participanteId
) {}
