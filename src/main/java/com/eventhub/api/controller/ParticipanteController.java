package com.eventhub.api.controller;

import com.eventhub.api.dto.IngressoResponseDTO;
import com.eventhub.api.dto.ParticipanteRequestDTO;
import com.eventhub.api.dto.ParticipanteResponseDTO;
import com.eventhub.api.service.IngressoService;
import com.eventhub.api.service.ParticipanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participantes")
@RequiredArgsConstructor
public class ParticipanteController {

    private final ParticipanteService participanteService;
    private final IngressoService ingressoService;

    @PostMapping
    public ResponseEntity<ParticipanteResponseDTO> cadastrar(@Valid @RequestBody ParticipanteRequestDTO dto) {
        ParticipanteResponseDTO participante = participanteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(participante);
    }

    @GetMapping("/{id}/ingressos")
    public ResponseEntity<List<IngressoResponseDTO>> listarIngressos(@PathVariable Long id) {
        List<IngressoResponseDTO> ingressos = ingressoService.listarPorParticipante(id);
        return ResponseEntity.ok(ingressos);
    }
}
