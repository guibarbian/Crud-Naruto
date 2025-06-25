package com.db.crud_naruto.controller.impl;

import com.db.crud_naruto.DTO.personagem.AprenderJutsuDto;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.controller.PersonagemController;
import com.db.crud_naruto.service.PersonagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/personagens")
@RequiredArgsConstructor
public class PersonagemControllerImpl implements PersonagemController {

    private final PersonagemService personagemService;

    @Override
    @GetMapping
    public Page<ResponsePersonagemDto> findAll(Pageable pageable) {
        return personagemService.findAll(pageable);
    }

    @Override
    @GetMapping("/{charId}")
    public ResponseEntity<ResponsePersonagemDto> findPersonagemById(@PathVariable Long charId) {
        return ResponseEntity.ok(personagemService.findPersonagemById(charId));
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponsePersonagemDto> createPersonagem(@RequestBody @Valid RequestPersonagemDto dto) {
        return ResponseEntity.status(201).body(personagemService.createPersonagem(dto));
    }

    @Override
    @PutMapping("/{charId}")
    public ResponseEntity<ResponsePersonagemDto> updatePersonagem(@PathVariable Long charId, @RequestBody @Valid RequestPersonagemDto dto) {
        return ResponseEntity.ok(personagemService.updatePersonagem(charId, dto));
    }

    @Override
    @PutMapping("/{charId}/aprenderJutsu")
    public ResponseEntity<ResponsePersonagemDto> aprenderJutsu(@PathVariable Long charId, @RequestBody @Valid AprenderJutsuDto dto) {
        return ResponseEntity.ok(personagemService.aprenderJutsu(charId, dto));
    }

    @Override
    @DeleteMapping("/{charId}")
    public ResponseEntity<String> deletePersonagem(@PathVariable Long charId) {
        personagemService.deletePersonagem(charId);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException e){

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
