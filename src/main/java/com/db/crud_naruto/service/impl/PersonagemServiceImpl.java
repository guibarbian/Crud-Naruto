package com.db.crud_naruto.service.impl;

import com.db.crud_naruto.DTO.personagem.AprenderJutsuDto;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.exceptions.BadRequestException;
import com.db.crud_naruto.exceptions.NotFoundException;
import com.db.crud_naruto.mapper.PersonagemMapper;
import com.db.crud_naruto.model.NinjaDeGenjutsu;
import com.db.crud_naruto.model.NinjaDeNinjutsu;
import com.db.crud_naruto.model.NinjaDeTaijutsu;
import com.db.crud_naruto.model.Personagem;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.service.PersonagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonagemServiceImpl implements PersonagemService {

    private final PersonagemRepository personagemRepository;
    private final PersonagemMapper personagemMapper;

    @Override
    public Page<ResponsePersonagemDto> findAll(Pageable pageable) {
        log.info("Buscando todos os personagens - Página: {}", pageable.getPageNumber());
        Page<Personagem> personagensPage = personagemRepository.findAll(pageable);
        return personagensPage.map(personagemMapper::map);
    }

    @Override
    public ResponsePersonagemDto findPersonagemById(Long charId) {
        Personagem personagem = buscarPersonagem(charId);

        log.debug("Personagem encontrado: {}", personagem);
        return personagemMapper.map(personagem);
    }

    @Override
    public ResponsePersonagemDto createPersonagem(@Valid RequestPersonagemDto dto) {
        log.info("Criando novo personagem: {}", dto.nome());
        Personagem novoPersonagem = construirNovoPersonagem(dto);

        Personagem personagemSalvo = personagemRepository.save(novoPersonagem);
        log.info("Personagem criado com ID: {}", personagemSalvo.getId());

        return personagemMapper.map(personagemSalvo);
    }

    @Override
    public ResponsePersonagemDto updatePersonagem(Long charId, @Valid RequestPersonagemDto dto) {
        Personagem personagem = buscarPersonagem(charId);

        Personagem novoPersonagem = construirNovoPersonagem(dto);
        novoPersonagem.setId(personagem.getId());

        Personagem personagemSalvo = personagemRepository.save(novoPersonagem);
        log.info("Personagem atualizado com sucesso. ID: {}", personagemSalvo.getId());

        return personagemMapper.map(personagemSalvo);
    }

    @Override
    public ResponsePersonagemDto aprenderJutsu(Long charId, @Valid AprenderJutsuDto dto) {
        Personagem personagem = buscarPersonagem(charId);

        validaJutsu(dto, personagem);

        personagem.getJutsus().put(dto.nomeJutsu(), dto.dano());

        log.info("Jutsu '{}' adicionado ao personagem com ID {}", dto.nomeJutsu(), charId);
        return personagemMapper.map(personagemRepository.save(personagem));
    }


    @Override
    public void deletePersonagem(Long charId) {
        Personagem personagem = buscarPersonagem(charId);

        log.info("Personagem com ID {} deletado com sucesso", personagem.getId());
        personagemRepository.delete(personagem);
    }

    private Personagem buscarPersonagem(Long charId){
        log.info("Buscando personagem com ID: {}", charId);

        return personagemRepository.findById(charId)
                .orElseThrow(() -> {
                    log.warn("Personagem com ID {} não encontrado", charId);
                    return new NotFoundException("Personagem não encontrado");
                });
    }

    private void validaJutsu(AprenderJutsuDto dto, Personagem personagem){
        if(personagem.getJutsus().containsKey(dto.nomeJutsu())){
            log.info("Personagem já conhece o jutsu '{}'", dto.nomeJutsu());
            throw new BadRequestException("Personagem já sabe o jutsu");
        }
    }

    private Personagem construirNovoPersonagem(RequestPersonagemDto dto){
        Personagem personagem = switch (dto.especialidade().toLowerCase()) {
            case "ninjutsu" -> new NinjaDeNinjutsu();
            case "taijutsu" -> new NinjaDeTaijutsu();
            case "genjutsu" -> new NinjaDeGenjutsu();
            default -> throw new IllegalArgumentException("Especialidade inválida");
        };

        personagem.setNome(dto.nome());
        personagem.setVida(dto.vida());
        personagem.setChakra(dto.chakra());
        personagem.setJutsus(dto.jutsus());

        return personagem;
    }

}
