package com.eventhub.api.service;

import com.eventhub.api.dto.EventoRequestDTO;
import com.eventhub.api.dto.EventoResponseDTO;
import com.eventhub.api.exception.EventoNaoEncontradoException;
import com.eventhub.api.model.Evento;
import com.eventhub.api.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoResponseDTO criar(EventoRequestDTO dto) {

        Evento evento = Evento.builder()
                .nome(dto.nome())
                .data(dto.data())
                .local(dto.local())
                .capacidade(dto.capacidade())
                .build();

        Evento salvo = eventoRepository.save(evento);
        log.info("Evento criado: id={}, nome={}", salvo.getId(), salvo.getNome());

        return EventoResponseDTO.gerarDTO(salvo);

    }


    public List<EventoResponseDTO> listarTodos() {

        return eventoRepository.findAll()
                .stream()
                .map(EventoResponseDTO::gerarDTO)
                .toList();

    }

    public EventoResponseDTO buscarPorId(Long id) {

        Evento evento = buscarEventoOuLancarExcecao(id);
        return EventoResponseDTO.gerarDTO(evento);

    }

    public EventoResponseDTO atualizar(Long id, EventoRequestDTO dto) {

        Evento evento = buscarEventoOuLancarExcecao(id);
        evento.setNome(dto.nome());
        evento.setData(dto.data());
        evento.setLocal(dto.local());
        evento.setCapacidade(dto.capacidade());
        
        Evento atualizado = eventoRepository.save(evento);
        log.info("Evento atualizado: id={}", atualizado.getId());

        return EventoResponseDTO.gerarDTO(atualizado);
    }

    public void deletar(Long id) {

        buscarEventoOuLancarExcecao(id);

        eventoRepository.deleteById(id);
        log.info("Evento deletado: id={}", id);

    }

    private Evento buscarEventoOuLancarExcecao(Long id) {

        return eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNaoEncontradoException(id));
                
    }
}
