package com.db.crud_naruto.controller.impl;

import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.controller.PersonagemController;
import com.db.crud_naruto.service.PersonagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personagens")
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
    public ResponseEntity<ResponsePersonagemDto> createPersonagem(@RequestBody RequestPersonagemDto dto) {
        return ResponseEntity.status(201).body(personagemService.createPersonagem(dto));
    }

    @Override
    @PutMapping("/{charId}")
    public ResponseEntity<ResponsePersonagemDto> updatePersonagem(@PathVariable Long charId, @RequestBody RequestPersonagemDto dto) {
        return ResponseEntity.ok(personagemService.updatePersonagem(charId, dto));
    }

    @Override
    @DeleteMapping("/{charId}")
    public ResponseEntity<String> deletePersonagem(@PathVariable Long charId) {
        personagemService.deletePersonagem(charId);
        return ResponseEntity.ok().build();
    }
}
