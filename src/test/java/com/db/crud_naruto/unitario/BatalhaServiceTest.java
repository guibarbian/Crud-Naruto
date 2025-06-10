package com.db.crud_naruto.unitario;

import com.db.crud_naruto.exceptions.NotFoundException;
import com.db.crud_naruto.model.*;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.service.impl.BatalhaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatalhaServiceTest {

    @Mock
    PersonagemRepository personagemRepository;

    @InjectMocks
    BatalhaServiceImpl batalhaService;

    NinjaDeNinjutsu ninjutsu = NinjaDeNinjutsu.builder().id(1L).nome("Sasuke Uchiha")
            .chakra(100).vida(100).jutsus(Map.of("Chidori", 20, "Kirin", 40)).build();
    NinjaDeGenjutsu genjutsu = NinjaDeGenjutsu.builder().id(2L).nome("Itachi Uchiha")
            .chakra(100).vida(100).jutsus(Map.of("Tsukuyomi", 40, "Shishirendan", 20)).build();
    NinjaDeTaijutsu taijutsu = NinjaDeTaijutsu.builder().id(3L).nome("Rock Lee")
            .chakra(100).vida(100).jutsus(Map.of("Lotus", 60)).build();

    @Test
    public void testGetPersonagem(){
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));

        Personagem personagem = batalhaService.findPersonagem(1L);

        assertEquals("Sasuke Uchiha", personagem.getNome());
        assertEquals(NinjaDeNinjutsu.class, personagem.getClass());
    }

    @Test
    public void testGetPersonagemNegativo(){
        when(personagemRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> batalhaService.findPersonagem(4L));
    }

    @Test
    public void testBatalhaPorVida(){
        ninjutsu.setVida(0);

        Personagem vencedor = batalhaService.batalha(ninjutsu, taijutsu);

        assertEquals("Rock Lee", vencedor.getNome());
    }

    @Test
    public void testBatalhaPorChakra(){
        taijutsu.setChakra(0);

        Personagem vencedor = batalhaService.batalha(ninjutsu, taijutsu);

        assertEquals("Sasuke Uchiha", vencedor.getNome());
    }

    @Test
    public void testIniciaBatalhaVencedorNinjutsu(){
        ninjutsu.setVida(0);

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));
        when(personagemRepository.findById(3L)).thenReturn(Optional.of(taijutsu));

        String resultado = batalhaService.iniciarBatalha(1L, 3L);

        assertEquals("Fim da batalha! Rock Lee venceu!", resultado);
    }

    @Test
    public void testIniciaBatalhaVencedorTaijutsu(){
        taijutsu.setChakra(0);

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));
        when(personagemRepository.findById(3L)).thenReturn(Optional.of(taijutsu));

        String resultado = batalhaService.iniciarBatalha(1L, 3L);

        assertEquals("Fim da batalha! Sasuke Uchiha venceu!", resultado);
    }
}
