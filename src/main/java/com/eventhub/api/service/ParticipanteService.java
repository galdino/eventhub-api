package com.eventhub.api.service;

import com.eventhub.api.dto.ParticipanteRequestDTO;
import com.eventhub.api.dto.ParticipanteResponseDTO;
import com.eventhub.api.exception.EmailDuplicadoException;
import com.eventhub.api.model.Participante;
import com.eventhub.api.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    public ParticipanteResponseDTO cadastrar(ParticipanteRequestDTO dto) {

        if (participanteRepository.existsByEmail(dto.email())) {
            throw new EmailDuplicadoException(dto.email());
        }

        Participante participante = Participante.builder()
                .nome(dto.nome())
                .email(dto.email())
                .build();

        Participante salvo = participanteRepository.save(participante);
        log.info("Participante cadastrado: id={}, email={}", salvo.getId(), salvo.getEmail());

        return ParticipanteResponseDTO.gerarDTO(salvo);
        
    }
}
