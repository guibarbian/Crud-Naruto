package com.db.crud_naruto.unitario;

import com.db.crud_naruto.model.NinjaDeGenjutsu;
import com.db.crud_naruto.model.NinjaDeNinjutsu;
import com.db.crud_naruto.model.NinjaDeTaijutsu;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.service.impl.BatalhaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testBatalhaPorVida(){
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));
        when(personagemRepository.findById(2L)).thenReturn(Optional.of(genjutsu));

        ninjutsu.setVida(0);

        String result = batalhaService.iniciarBatalha(1L, 2L);

        assertEquals("Fim da batalha! Itachi Uchiha venceu!", result);
    }

    @Test
    public void testBatalhaPorChakra(){
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));
        when(personagemRepository.findById(2L)).thenReturn(Optional.of(genjutsu));

        genjutsu.setChakra(0);

        String result = batalhaService.iniciarBatalha(1L, 2L);

        assertEquals("Fim da batalha! Sasuke Uchiha venceu!", result);
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
