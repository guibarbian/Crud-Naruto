package com.db.crud_naruto.service;

import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonagemService {

    Page<ResponsePersonagemDto> findAll(Pageable pageable);

    ResponsePersonagemDto findPersonagemById(Long charId);

    ResponsePersonagemDto createPersonagem(RequestPersonagemDto dto);

    ResponsePersonagemDto updatePersonagem(Long charId, RequestPersonagemDto dto);

    ResponsePersonagemDto aprenderJutsu(Long charId, String nomeJutsu, Integer dano);

    void deletePersonagem(Long charId);
}
