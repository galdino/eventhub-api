package com.eventhub.api.controller;

import com.eventhub.api.dto.EventoRequestDTO;
import com.eventhub.api.dto.EventoResponseDTO;
import com.eventhub.api.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponseDTO> criar(@Valid @RequestBody EventoRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.criar(dto));

    }

    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(eventoService.listarTodos());

    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(eventoService.buscarPorId(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody EventoRequestDTO dto) {

        return ResponseEntity.ok(eventoService.atualizar(id, dto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
        
    }
}
