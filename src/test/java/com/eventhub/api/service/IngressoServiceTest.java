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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngressoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private ParticipanteRepository participanteRepository;

    @Mock
    private IngressoRepository ingressoRepository;

    @InjectMocks
    private IngressoService ingressoService;

    @Test
    void deveComprarIngressoEDecrementarCapacidade() {

        Evento evento = Evento.builder()
                .id(1L).nome("Show").local("Arena").capacidade(10)
                .data(LocalDateTime.now().plusDays(30))
                .build();

        Participante participante = Participante.builder()
                .id(2L).nome("Ana").email("ana@email.com")
                .build();

        Ingresso ingressoSalvo = Ingresso.builder()
                .id(99L).evento(evento).participante(participante)
                .dataCompra(LocalDateTime.now())
                .build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(participanteRepository.findById(2L)).thenReturn(Optional.of(participante));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);
        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingressoSalvo);

        IngressoResponseDTO resposta = ingressoService.comprar(new IngressoRequestDTO(1L, 2L));

        ArgumentCaptor<Evento> eventoCaptor = ArgumentCaptor.forClass(Evento.class);
        verify(eventoRepository).save(eventoCaptor.capture());
        assertThat(eventoCaptor.getValue().getCapacidade()).isEqualTo(9);

        verify(ingressoRepository).save(any(Ingresso.class));

        assertThat(resposta.id()).isEqualTo(99L);
        assertThat(resposta.eventoId()).isEqualTo(1L);
        assertThat(resposta.participanteId()).isEqualTo(2L);

    }


    @Test
    void deveLancarExcecaoQuandoEventoEstiverLotado() {

        Evento eventoLotado = Evento.builder()
                .id(1L).nome("Show").local("Arena").capacidade(0)
                .data(LocalDateTime.now().plusDays(30))
                .build();

        Participante participante = Participante.builder()
                .id(2L).nome("Ana").email("ana@email.com")
                .build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(eventoLotado));
        when(participanteRepository.findById(2L)).thenReturn(Optional.of(participante));

        assertThatThrownBy(() -> ingressoService.comprar(new IngressoRequestDTO(1L, 2L)))
                .isInstanceOf(EventoLotadoException.class)
                .hasMessageContaining("1");

        verify(ingressoRepository, never()).save(any());

    }

    @Test
    void deveLancarExcecaoQuandoEventoNaoForEncontrado() {

        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ingressoService.comprar(new IngressoRequestDTO(99L, 2L)))
                .isInstanceOf(EventoNaoEncontradoException.class);

        verify(ingressoRepository, never()).save(any());

    }

    @Test
    void deveLancarExcecaoQuandoParticipanteNaoForEncontrado() {

        Evento evento = Evento.builder()
                .id(1L).nome("Show").local("Arena").capacidade(5)
                .data(LocalDateTime.now().plusDays(30))
                .build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(participanteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ingressoService.comprar(new IngressoRequestDTO(1L, 99L)))
                .isInstanceOf(ParticipanteNaoEncontradoException.class);

        verify(ingressoRepository, never()).save(any());

    }

    @Test
    void deveListarIngressosDoParticipante() {

        Participante participante = Participante.builder()
                .id(2L).nome("Ana").email("ana@email.com")
                .build();

        Evento evento = Evento.builder()
                .id(1L).nome("Show").local("Arena").capacidade(8)
                .data(LocalDateTime.now().plusDays(30))
                .build();

        Ingresso ingresso = Ingresso.builder()
                .id(10L).evento(evento).participante(participante)
                .dataCompra(LocalDateTime.now())
                .build();

        when(participanteRepository.findById(2L)).thenReturn(Optional.of(participante));
        when(ingressoRepository.findByParticipanteId(2L)).thenReturn(List.of(ingresso));

        List<IngressoResponseDTO> resultado = ingressoService.listarPorParticipante(2L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).participanteId()).isEqualTo(2L);
        assertThat(resultado.get(0).eventoId()).isEqualTo(1L);

    }

    @Test
    void deveLancarExcecaoAoListarIngressosDeParticipanteInexistente() {

        when(participanteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ingressoService.listarPorParticipante(99L))
                .isInstanceOf(ParticipanteNaoEncontradoException.class);

        verify(ingressoRepository, never()).findByParticipanteId(anyLong());
        
    }
}
