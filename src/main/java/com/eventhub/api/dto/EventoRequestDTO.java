package com.eventhub.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record EventoRequestDTO(

    @NotBlank(message = "O nome do evento é obrigatório")
    String nome,

    @NotNull(message = "A data do evento é obrigatória")
    @Future(message = "A data do evento não pode ser no passado")
    LocalDateTime data,

    @NotBlank(message = "O local do evento é obrigatório")
    String local,

    @NotNull(message = "A capacidade é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    Integer capacidade
) {}
