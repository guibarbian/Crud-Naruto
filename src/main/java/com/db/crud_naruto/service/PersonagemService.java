package com.db.crud_naruto.service;

import com.db.crud_naruto.DTO.personagem.AprenderJutsuDto;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonagemService {

    Page<ResponsePersonagemDto> findAll(Pageable pageable);

    ResponsePersonagemDto findPersonagemById(Long charId);

    ResponsePersonagemDto createPersonagem(@Valid RequestPersonagemDto dto);

    ResponsePersonagemDto updatePersonagem(Long charId, @Valid RequestPersonagemDto dto);

    ResponsePersonagemDto aprenderJutsu(Long charId, @Valid AprenderJutsuDto dto);

    void deletePersonagem(Long charId);
}
