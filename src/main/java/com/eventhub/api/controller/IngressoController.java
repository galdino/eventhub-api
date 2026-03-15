package com.eventhub.api.controller;

import com.eventhub.api.dto.IngressoRequestDTO;
import com.eventhub.api.dto.IngressoResponseDTO;
import com.eventhub.api.service.IngressoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingressos")
@RequiredArgsConstructor
public class IngressoController {

    private final IngressoService ingressoService;

    @PostMapping
    public ResponseEntity<IngressoResponseDTO> comprar(@Valid @RequestBody IngressoRequestDTO dto) {
        IngressoResponseDTO ingresso = ingressoService.comprar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingresso);
    }
}
