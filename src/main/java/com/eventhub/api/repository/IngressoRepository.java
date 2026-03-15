package com.eventhub.api.repository;

import com.eventhub.api.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
    List<Ingresso> findByParticipanteId(Long participanteId);
}
