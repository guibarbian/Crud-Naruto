package com.db.crud_naruto.unitario;

import com.db.crud_naruto.model.NinjaDeGenjutsu;
import com.db.crud_naruto.model.NinjaDeNinjutsu;
import com.db.crud_naruto.model.NinjaDeTaijutsu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PersonagemModelsTest {

    NinjaDeNinjutsu ninjutsu = NinjaDeNinjutsu.builder().id(1L).nome("Sasuke Uchiha")
            .chakra(100).vida(100).jutsus(Map.of("Chidori", 20, "Kirin", 40)).build();
    NinjaDeGenjutsu genjutsu = NinjaDeGenjutsu.builder().id(2L).nome("Itachi Uchiha")
            .chakra(100).vida(100).jutsus(Map.of("Tsukuyomi", 40, "Shishirendan", 20)).build();
    NinjaDeTaijutsu taijutsu = NinjaDeTaijutsu.builder().id(3L).nome("Rock Lee")
            .chakra(100).vida(100).jutsus(Map.of("Lotus", 60)).build();

    @Test
    public void testUsarNinjutsu(){
        assertEquals("Sasuke Uchiha está atacando com Ninjutsu", ninjutsu.usarJutsu());
    }

    @Test
    public void testDesviarNinjutsu(){
        assertEquals("Sasuke Uchiha está desviando usando suas habilidades de Ninjutsu", ninjutsu.desviar());
    }

    @Test
    public void testUsarGenjutsu(){
        assertEquals("Itachi Uchiha está atacando com Genjutsu", genjutsu.usarJutsu());
    }

    @Test
    public void testDesviarGenjutsu(){
        assertEquals("Itachi Uchiha está desviando usando suas habilidades de Genjutsu", genjutsu.desviar());
    }

    @Test
    public void testUsarTaijutsu(){
        assertEquals("Rock Lee está atacando com Taijutsu", taijutsu.usarJutsu());
    }

    @Test
    public void testDesviarTaijutsu(){
        assertEquals("Rock Lee está desviando usando suas habilidades de Taijutsu", taijutsu.desviar());
    }
}
