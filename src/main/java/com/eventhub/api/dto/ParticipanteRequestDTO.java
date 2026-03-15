package com.eventhub.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ParticipanteRequestDTO(
    @NotBlank(message = "O nome do participante é obrigatório")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email
) {}
