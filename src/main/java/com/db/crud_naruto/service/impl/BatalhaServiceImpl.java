package com.db.crud_naruto.service.impl;

import com.db.crud_naruto.exceptions.NotFoundException;
import com.db.crud_naruto.model.Personagem;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.service.BatalhaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatalhaServiceImpl implements BatalhaService {

    final PersonagemRepository personagemRepository;

    @Override
    public String iniciarBatalha(Long idDesafiante, Long idDesafiado) {
        log.info("Iniciando batalha entre personagens com ids {} e {}", idDesafiante, idDesafiado);

        Personagem ninja1 = buscarPersonagem(idDesafiante);
        Personagem ninja2 = buscarPersonagem(idDesafiado);

        log.info("Começa a batalha");
        Personagem vencedor = batalha(ninja1, ninja2);

        return "Fim da batalha! " + vencedor.getNome() + " venceu!";
    }

    private Personagem batalha(Personagem ninja1, Personagem ninja2){
        log.info("{} vs {}, comecem a batalha!", ninja1.getNome(), ninja2.getNome());
        int turno = 1;

        while(ambosPodemLutar(ninja1, ninja2)){
            if(turno % 2 == 0){
                rodarTurno(ninja1, ninja2, turno);
            } else{
                rodarTurno(ninja2, ninja1, turno);
            }
            turno++;
        }
        Personagem vencedor = determinaVencedor(ninja1, ninja2);
        log.info("{} venceu a batalha!", vencedor.getNome());
        return vencedor;
    }

    private Personagem buscarPersonagem(Long charId){
        log.info("Buscando personagem com id {}", charId);
        return personagemRepository.findById(charId)
                .orElseThrow(() -> {
                    log.error("Personagem com ID: {} não encontrado", charId);
                    return new NotFoundException("Personagem não encontrado");
                });
    }

    private boolean ambosPodemLutar(Personagem ninja1, Personagem ninja2){
        return ninja1.getVida() > 0 && ninja1.getChakra() > 0 &&
                ninja2.getVida() > 0 && ninja2.getChakra() > 0;
    }

    private double calculaChanceDeDesvio(int vidaDefensor, int chakraAtacante){
        return((double) chakraAtacante / vidaDefensor) * 60;
    }

    private void rodarTurno(Personagem atacante, Personagem defensor, int turno){
        log.info("Turno {}: {} ataca {}", turno, atacante.getNome(), defensor.getNome());

        double chanceDeDesvio = calculaChanceDeDesvio(defensor.getVida(), atacante.getChakra());
        double chanceDeAtaque = (Math.random() * 100);

        log.info("Chance de ataque: {}%, Chance de desvio: {}%", chanceDeAtaque, chanceDeDesvio);
        if(chanceDeAtaque > chanceDeDesvio){
            realizaAtaque(atacante, defensor);
        } else{
            realizaDesviar(atacante, defensor);
        }
    }

    private void realizaAtaque(Personagem atacante, Personagem defensor){
        log.info("{} acerta o ataque!", atacante.getNome());
        defensor.setVida(defensor.getVida() - 20);
        atacante.setChakra(atacante.getChakra() - 10);
    }

    private void realizaDesviar(Personagem atacante, Personagem defensor){
        log.info("{} desvia do ataque!", defensor.getNome());
        atacante.setChakra(atacante.getChakra() - 10);
    }

    private Personagem determinaVencedor(Personagem ninja1, Personagem ninja2){
        if(ninja1.getVida() <= 0 || ninja1.getChakra() <= 0){
            return ninja2;
        }

        return ninja1;
    }
}
