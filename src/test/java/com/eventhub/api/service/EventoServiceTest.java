package com.eventhub.api.service;

import com.eventhub.api.dto.EventoRequestDTO;
import com.eventhub.api.dto.EventoResponseDTO;
import com.eventhub.api.exception.EventoNaoEncontradoException;
import com.eventhub.api.model.Evento;
import com.eventhub.api.repository.EventoRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    @Test
    void deveCriarEventoERetornarDTO() {

        EventoRequestDTO dto = new EventoRequestDTO(
                "Show de Rock", LocalDateTime.now().plusDays(10),
                "Arena", 200);

        Evento salvo = Evento.builder()
                .id(1L).nome("Show de Rock").local("Arena").capacidade(200)
                .data(dto.data())
                .build();

        when(eventoRepository.save(any(Evento.class))).thenReturn(salvo);

        EventoResponseDTO resposta = eventoService.criar(dto);

        ArgumentCaptor<Evento> captor = ArgumentCaptor.forClass(Evento.class);
        verify(eventoRepository).save(captor.capture());

        assertThat(captor.getValue().getNome()).isEqualTo("Show de Rock");
        assertThat(captor.getValue().getLocal()).isEqualTo("Arena");
        assertThat(captor.getValue().getCapacidade()).isEqualTo(200);

        assertThat(resposta.id()).isEqualTo(1L);
        assertThat(resposta.nome()).isEqualTo("Show de Rock");

    }

    @Test
    void deveRetornarListaDeEventos() {

        Evento e1 = Evento.builder().id(1L).nome("Festa").local("Club")
                .capacidade(50).data(LocalDateTime.now().plusDays(5)).build();
        Evento e2 = Evento.builder().id(2L).nome("Show").local("Arena")
                .capacidade(500).data(LocalDateTime.now().plusDays(15)).build();

        when(eventoRepository.findAll()).thenReturn(List.of(e1, e2));

        List<EventoResponseDTO> resultado = eventoService.listarTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(1).id()).isEqualTo(2L);

    }

    @Test
    void deveBuscarEventoPorId() {

        Evento evento = Evento.builder().id(1L).nome("Show").local("Arena")
                .capacidade(100).data(LocalDateTime.now().plusDays(7)).build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        EventoResponseDTO resposta = eventoService.buscarPorId(1L);

        assertThat(resposta.id()).isEqualTo(1L);
        assertThat(resposta.nome()).isEqualTo("Show");

    }

    @Test
    void deveLancarExcecaoAoBuscarEventoInexistente() {

        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventoService.buscarPorId(99L))
                .isInstanceOf(EventoNaoEncontradoException.class)
                .hasMessageContaining("99");

    }

    
    @Test
    void deveLancarExcecaoAoAtualizarEventoInexistente() {

        EventoRequestDTO dto = new EventoRequestDTO(
                "Nome", LocalDateTime.now().plusDays(10), "Local", 100);

        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventoService.atualizar(99L, dto))
                .isInstanceOf(EventoNaoEncontradoException.class);

        verify(eventoRepository, never()).save(any());

    }

    @Test
    void deveDeletarEventoExistente() {

        Evento evento = Evento.builder().id(1L).nome("Show").local("Arena")
                .capacidade(100).data(LocalDateTime.now().plusDays(7)).build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        eventoService.deletar(1L);

        verify(eventoRepository).deleteById(1L);

    }

    @Test
    void deveLancarExcecaoAoDeletarEventoInexistente() {

        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventoService.deletar(99L))
                .isInstanceOf(EventoNaoEncontradoException.class);

        verify(eventoRepository, never()).deleteById(any());
        
    }
}
