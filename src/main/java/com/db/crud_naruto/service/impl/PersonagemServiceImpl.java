package com.db.crud_naruto.service.impl;

import com.db.crud_naruto.DTO.personagem.AprenderJutsuDto;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.exceptions.BadRequestException;
import com.db.crud_naruto.exceptions.NotFoundException;
import com.db.crud_naruto.model.NinjaDeGenjutsu;
import com.db.crud_naruto.model.NinjaDeNinjutsu;
import com.db.crud_naruto.model.NinjaDeTaijutsu;
import com.db.crud_naruto.model.Personagem;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.service.PersonagemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonagemServiceImpl implements PersonagemService {

    private final PersonagemRepository personagemRepository;

    @Override
    public Page<ResponsePersonagemDto> findAll(Pageable pageable) {
        log.info("Buscando todos os personagens - Página: {}", pageable.getPageNumber());
        Page<Personagem> personagensPage = personagemRepository.findAll(pageable);
        return personagensPage.map(Personagem::toDto);
    }

    @Override
    public ResponsePersonagemDto findPersonagemById(Long charId) {
        log.info("Buscando personagem com ID: {}", charId);
        Optional<Personagem> personagem = personagemRepository.findById(charId);

        if(personagem.isEmpty()){
            log.warn("Personagem com ID {} não encontrado", charId);
            throw new NotFoundException("Personagem não encontrado");
        }

        log.debug("Personagem encontrado: {}", personagem.get());
        return personagem.get().toDto();
    }

    @Override
    public ResponsePersonagemDto createPersonagem(RequestPersonagemDto dto) {
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
        Map<String,Integer> jutsus = dto.jutsus();

        if(jutsus.isEmpty()){
            log.error("Ninja não foi criado pois não possui jutsus");
            throw new BadRequestException("Ninja deve ter ao menos um Jutsu");
        }

        novoPersonagem.setJutsus(jutsus);

        Personagem personagemSalvo = personagemRepository.save(novoPersonagem);
        log.info("Personagem criado com ID: {}", personagemSalvo.getId());

        return personagemSalvo.toDto();
    }

    @Override
    public ResponsePersonagemDto updatePersonagem(Long charId, RequestPersonagemDto dto) {
        log.info("Atualizando personagem com ID: {}", charId);
        Optional<Personagem> personagemExistente = personagemRepository.findById(charId);

        if(personagemExistente.isEmpty()){
            log.warn("Personagem com ID {} não encontrado para atualização", charId);
            throw new NotFoundException("Personagem não encontrado");
        }

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

        Map<String,Integer> jutsus = dto.jutsus();

        if(jutsus.isEmpty()){
            log.error("Ninja não foi atualizado pois não possui jutsus");
            throw new BadRequestException("Ninja deve ter ao menos um Jutsu");
        }

        novoPersonagem.setJutsus(jutsus);

        Personagem personagemSalvo = personagemRepository.save(novoPersonagem);
        log.info("Personagem atualizado com sucesso. ID: {}", personagemSalvo.getId());

        return personagemSalvo.toDto();
    }

    @Override
    public ResponsePersonagemDto aprenderJutsu(Long charId, AprenderJutsuDto dto) {
        log.info("Checando se personagem com id {} existe", charId);

        Personagem personagem = personagemRepository.findById(charId)
                .orElseThrow(() -> {
                    log.warn("Personagem com ID {} não encontrado", charId);
                    return new NotFoundException("Personagem não encontrado");
                });

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
        return personagemRepository.save(personagem).toDto();
    }


    @Override
    public void deletePersonagem(Long charId) {
        log.info("Deletando personagem com ID: {}", charId);
        Optional<Personagem> personagemExistente = personagemRepository.findById(charId);

        if(personagemExistente.isEmpty()){
            log.warn("Personagem com ID {} não encontrado para exclusão", charId);
            throw new NotFoundException("Personagem não encontrado");
        }

        log.info("Personagem com ID {} deletado com sucesso", charId);
        personagemRepository.deleteById(charId);
    }
}
