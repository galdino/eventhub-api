package com.eventhub.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do evento é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Future(message = "A data do evento não pode ser no passado")
    @Column(nullable = false)
    private LocalDateTime data;

    @NotBlank(message = "O local do evento é obrigatório")
    @Column(nullable = false)
    private String local;

    @NotNull(message = "A capacidade é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    @Column(nullable = false)
    private Integer capacidade;
    
}
