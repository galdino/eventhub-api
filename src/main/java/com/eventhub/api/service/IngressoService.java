package com.eventhub.api.service;

import com.eventhub.api.dto.IngressoRequestDTO;
import com.eventhub.api.dto.IngressoResponseDTO;
import com.eventhub.api.exception.EventoLotadoException;
import com.eventhub.api.exception.EventoNaoEncontradoException;
import com.eventhub.api.exception.ParticipanteNaoEncontradoException;
import com.eventhub.api.model.Evento;
import com.eventhub.api.model.Ingresso;
import com.eventhub.api.model.Participante;
import com.eventhub.api.repository.EventoRepository;
import com.eventhub.api.repository.IngressoRepository;
import com.eventhub.api.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngressoService {

    private final EventoRepository eventoRepository;
    private final ParticipanteRepository participanteRepository;
    private final IngressoRepository ingressoRepository;

    @Transactional
    public IngressoResponseDTO comprar(IngressoRequestDTO dto) {

        Evento evento = buscarEventoOuLancarExcecao(dto.eventoId());
        Participante participante = buscarParticipanteOuLancarExcecao(dto.participanteId());

        validarCapacidadeDisponivel(evento);

        evento.setCapacidade(evento.getCapacidade() - 1);
        eventoRepository.save(evento);

        Ingresso ingresso = Ingresso.builder()
                .evento(evento)
                .participante(participante)
                .build();
        Ingresso salvo = ingressoRepository.save(ingresso);
        log.info("Ingresso comprado: id={}, evento={}, participante={}", salvo.getId(), evento.getNome(), participante.getNome());
        
        return IngressoResponseDTO.gerarDTO(salvo);

    }

    public List<IngressoResponseDTO> listarPorParticipante(Long participanteId) {

        buscarParticipanteOuLancarExcecao(participanteId);
        return ingressoRepository.findByParticipanteId(participanteId)
                .stream()
                .map(IngressoResponseDTO::gerarDTO)
                .toList();
                
    }

    private Evento buscarEventoOuLancarExcecao(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNaoEncontradoException(id));
    }

    private Participante buscarParticipanteOuLancarExcecao(Long id) {
        return participanteRepository.findById(id)
                .orElseThrow(() -> new ParticipanteNaoEncontradoException(id));
    }

    private void validarCapacidadeDisponivel(Evento evento) {
        if (evento.getCapacidade() == null || evento.getCapacidade() <= 0) {
            throw new EventoLotadoException(evento.getId());
        }
    }
}
