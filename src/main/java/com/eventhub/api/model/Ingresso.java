package com.eventhub.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ingressos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @Column(nullable = false)
    private LocalDateTime dataCompra;

    @PrePersist
    private void preencherDataCompra() {
        this.dataCompra = LocalDateTime.now();
    }
}
