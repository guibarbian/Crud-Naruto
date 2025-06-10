package com.db.crud_naruto.service.impl;

import com.db.crud_naruto.exceptions.NotFoundException;
import com.db.crud_naruto.model.Personagem;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.service.BatalhaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatalhaServiceImpl implements BatalhaService {

    final PersonagemRepository personagemRepository;

    @Override
    public String iniciarBatalha(Long idDesafiante, Long idDesafiado) {
        log.info("Iniciando batalha entre personagens com ids {} e {}", idDesafiante, idDesafiado);

        Personagem ninja1 = findPersonagem(idDesafiante);
        Personagem ninja2 = findPersonagem(idDesafiado);

        log.info("Começa a batalha");
        Personagem vencedor = batalha(ninja1, ninja2);

        return "Fim da batalha! " + vencedor.getNome() + " venceu!";
    }

    public Personagem findPersonagem(Long id){
        log.info("Buscando personagem com id {}", id);
        Optional<Personagem> personagem = personagemRepository.findById(id);

        if(personagem.isEmpty()){
            log.error("Personagem com id {} não encontrado", id);
            throw new NotFoundException("Personagem não encontrado");
        }

        return personagem.get();
    }

    public Personagem batalha(Personagem ninja1, Personagem ninja2){
        int turno = 1;

        while (ninja1.getVida() > 0 && ninja2.getVida() > 0 && ninja1.getChakra() > 0 && ninja2.getChakra() > 0){
            if(turno % 2 == 0){
                log.info("Turno {}: {} ataca {}", turno, ninja1.getNome(), ninja2.getNome());

                double chanceDeDesvio = ((double)ninja1.getChakra() / (double)ninja2.getVida()) * 60;
                double chanceDeAtaque = (Math.random() * 100);

                log.info("Chance de ataque: {}%, Chance de desvio: {}%", chanceDeAtaque, chanceDeDesvio);

                if(chanceDeAtaque > chanceDeDesvio){
                    log.info("{} acerta o ataque!", ninja1.getNome());
                    ninja2.setVida(ninja2.getVida() - 20);
                    ninja1.setChakra(ninja1.getChakra() - 10);
                } else{
                    log.info("{} desvia do ataque!", ninja2.getNome());
                    ninja1.setChakra(ninja1.getChakra() - 10);
                }
            } else {
                log.info("Turno {}: {} ataca {}", turno, ninja2.getNome(), ninja1.getNome());

                double chanceDeDesvio = ((double)ninja2.getChakra() / (double)ninja1.getVida()) * 60;
                double chanceDeAtaque = (Math.random() * 100);


                log.info("Chance de desvio: {}%, Chance de ataque: {}%", chanceDeDesvio, chanceDeAtaque);
                if(chanceDeAtaque > chanceDeDesvio){
                    log.info("{} acerta o ataque!", ninja2.getNome());
                    ninja1.setVida(ninja1.getVida() - 20);
                    ninja2.setChakra(ninja2.getChakra() - 10);
                } else{
                    log.info("{} desvia do ataque!", ninja1.getNome());
                    ninja2.setChakra(ninja2.getChakra() - 10);
                }
            }
            turno++;
        }

        if(ninja1.getVida() <= 0 || ninja1.getChakra() <= 0){
            log.info("{} venceu a batalha!", ninja2.getNome());
            return ninja2;
        } else {
            log.info("{} venceu a batalha!", ninja1.getNome());
            return ninja1;
        }
    }
}
