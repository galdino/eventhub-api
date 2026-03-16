package com.eventhub.api.service;

import com.eventhub.api.dto.ParticipanteRequestDTO;
import com.eventhub.api.dto.ParticipanteResponseDTO;
import com.eventhub.api.exception.EmailDuplicadoException;
import com.eventhub.api.model.Participante;
import com.eventhub.api.repository.ParticipanteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipanteServiceTest {

    @Mock
    private ParticipanteRepository participanteRepository;

    @InjectMocks
    private ParticipanteService participanteService;

    @Test
    void deveCadastrarParticipanteERetornarDTO() {

        ParticipanteRequestDTO dto = new ParticipanteRequestDTO("Ana", "ana@email.com");

        Participante salvo = Participante.builder()
                .id(1L).nome("Ana").email("ana@email.com")
                .build();

        when(participanteRepository.existsByEmail("ana@email.com")).thenReturn(false);
        when(participanteRepository.save(any(Participante.class))).thenReturn(salvo);

        ParticipanteResponseDTO resposta = participanteService.cadastrar(dto);

        ArgumentCaptor<Participante> captor = ArgumentCaptor.forClass(Participante.class);
        verify(participanteRepository).save(captor.capture());
        assertThat(captor.getValue().getNome()).isEqualTo("Ana");
        assertThat(captor.getValue().getEmail()).isEqualTo("ana@email.com");

        assertThat(resposta.id()).isEqualTo(1L);
        assertThat(resposta.nome()).isEqualTo("Ana");
        assertThat(resposta.email()).isEqualTo("ana@email.com");

    }

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverCadastrado() {

        ParticipanteRequestDTO dto = new ParticipanteRequestDTO("João", "joao@email.com");

        when(participanteRepository.existsByEmail("joao@email.com")).thenReturn(true);

        assertThatThrownBy(() -> participanteService.cadastrar(dto))
                .isInstanceOf(EmailDuplicadoException.class)
                .hasMessageContaining("joao@email.com");

        verify(participanteRepository, never()).save(any());

    }
    
}
