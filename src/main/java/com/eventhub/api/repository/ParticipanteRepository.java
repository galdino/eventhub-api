package com.eventhub.api.repository;

import com.eventhub.api.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    Optional<Participante> findByEmail(String email);
    boolean existsByEmail(String email);
}
