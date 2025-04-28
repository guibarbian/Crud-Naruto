package com.db.crud_naruto.service.impl;

import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
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
        log.info("Criando novo personagem: {}", dto.getNome());
        Personagem novoPersonagem = switch(dto.getEspecialidade().toLowerCase()){
            case "ninjutsu" -> new NinjaDeNinjutsu();
            case "taijutsu" -> new NinjaDeTaijutsu();
            case "genjutsu" -> new NinjaDeGenjutsu();
            default -> {
                log.error("Especialidade inválida: {}", dto.getEspecialidade());
                throw new IllegalArgumentException("Especialidade inválida");
            }

        };

        novoPersonagem.setNome(dto.getNome());
        novoPersonagem.setIdade(dto.getIdade());
        novoPersonagem.setAldeia(dto.getAldeia());
        novoPersonagem.setChakra(dto.getChakra());
        novoPersonagem.setJutsus(dto.getJutsus());

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

        Personagem novoPersonagem = switch(dto.getEspecialidade().toLowerCase()){
            case "ninjutsu" -> new NinjaDeNinjutsu();
            case "taijutsu" -> new NinjaDeTaijutsu();
            case "genjutsu" -> new NinjaDeGenjutsu();
            default -> {
                log.error("Especialidade inválida: {}", dto.getEspecialidade());
                throw new IllegalArgumentException("Especialidade inválida");
            }
        };

        novoPersonagem.setId(charId);
        novoPersonagem.setNome(dto.getNome());
        novoPersonagem.setIdade(dto.getIdade());
        novoPersonagem.setAldeia(dto.getAldeia());
        novoPersonagem.setChakra(dto.getChakra());
        novoPersonagem.setJutsus(dto.getJutsus());

        Personagem personagemSalvo = personagemRepository.save(novoPersonagem);
        log.info("Personagem atualizado com sucesso. ID: {}", personagemSalvo.getId());

        return personagemSalvo.toDto();
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
