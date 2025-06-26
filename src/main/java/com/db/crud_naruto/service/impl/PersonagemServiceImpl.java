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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Optional;

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
        Personagem novoPersonagem = switch(dto.especialidade().toLowerCase()){
            case "ninjutsu" -> new NinjaDeNinjutsu();
            case "taijutsu" -> new NinjaDeTaijutsu();
            case "genjutsu" -> new NinjaDeGenjutsu();
            default -> {
                log.error("Especialidade inválida: {}", dto.especialidade());
                throw new IllegalArgumentException("Especialidade inválida");
            }

        };

        novoPersonagem.setNome(dto.nome());
        novoPersonagem.setVida(dto.vida());
        novoPersonagem.setChakra(dto.chakra());
        novoPersonagem.setJutsus(dto.jutsus());

        Personagem personagemSalvo = personagemRepository.save(novoPersonagem);
        log.info("Personagem criado com ID: {}", personagemSalvo.getId());

        return personagemMapper.map(personagemSalvo);
    }

    @Override
    public ResponsePersonagemDto updatePersonagem(Long charId, @Valid RequestPersonagemDto dto) {
        Personagem personagem = buscarPersonagem(charId);

        Personagem novoPersonagem = switch(dto.especialidade().toLowerCase()){
            case "ninjutsu" -> new NinjaDeNinjutsu();
            case "taijutsu" -> new NinjaDeTaijutsu();
            case "genjutsu" -> new NinjaDeGenjutsu();
            default -> {
                log.error("Especialidade inválida: {}", dto.especialidade());
                throw new IllegalArgumentException("Especialidade inválida");
            }
        };

        novoPersonagem.setId(charId);
        novoPersonagem.setNome(dto.nome());
        novoPersonagem.setVida(dto.vida());
        novoPersonagem.setChakra(dto.chakra());
        novoPersonagem.setJutsus(dto.jutsus());

        Personagem personagemSalvo = personagemRepository.save(novoPersonagem);
        log.info("Personagem atualizado com sucesso. ID: {}", personagemSalvo.getId());

        return personagemMapper.map(personagemSalvo);
    }

    @Override
    public ResponsePersonagemDto aprenderJutsu(Long charId, @Valid AprenderJutsuDto dto) {
        Personagem personagem = buscarPersonagem(charId);

        if (dto.nomeJutsu() == null || dto.dano() == null || dto.dano() <= 0) {
            log.warn("Dados inválidos para aprendizado de jutsu: nome={}, dano={}", dto.nomeJutsu(), dto.dano());
            throw new BadRequestException("Nome do jutsu ou dano inválido");
        }

        Map<String, Integer> jutsus = personagem.getJutsus();

        if (jutsus.containsKey(dto.nomeJutsu())) {
            log.info("Personagem já conhece o jutsu '{}'", dto.nomeJutsu());
            throw new BadRequestException("Personagem já sabe o jutsu");
        }

        jutsus.put(dto.nomeJutsu(), dto.dano());
        personagem.setJutsus(jutsus);

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

}
