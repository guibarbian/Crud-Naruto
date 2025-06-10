package com.db.crud_naruto.unitario;

import com.db.crud_naruto.DTO.personagem.AprenderJutsuDto;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.exceptions.BadRequestException;
import com.db.crud_naruto.exceptions.NotFoundException;
import com.db.crud_naruto.model.*;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.service.impl.PersonagemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonagemServiceTest {

    @Mock
    private PersonagemRepository personagemRepository;

    @InjectMocks
    private PersonagemServiceImpl personagemService;

    NinjaDeNinjutsu ninjutsu = NinjaDeNinjutsu.builder().id(1L).nome("Sasuke Uchiha")
            .chakra(100).vida(100).jutsus(Map.of("Chidori", 20, "Kirin", 40)).build();
    NinjaDeGenjutsu genjutsu = NinjaDeGenjutsu.builder().id(2L).nome("Itachi Uchiha")
            .chakra(100).vida(100).jutsus(Map.of("Tsukuyomi", 40, "Shishirendan", 20)).build();
    NinjaDeTaijutsu taijutsu = NinjaDeTaijutsu.builder().id(3L).nome("Rock Lee")
            .chakra(100).vida(100).jutsus(Map.of("Lotus", 60)).build();

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Personagem> personagens = List.of(ninjutsu, genjutsu, taijutsu);
        Page<Personagem> page = new PageImpl<>(personagens);

        when(personagemRepository.findAll(pageable)).thenReturn(page);

        Page<ResponsePersonagemDto> result = personagemService.findAll(pageable);

        assertEquals(3, result.getTotalElements());
        verify(personagemRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindPersonagemById_Success() {
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));

        ResponsePersonagemDto result = personagemService.findPersonagemById(1L);

        assertEquals("Sasuke Uchiha", result.nome());
        verify(personagemRepository, times(1)).findById(1L);
    }

    @Test
    void testFindPersonagemById_NotFound() {
        when(personagemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> personagemService.findPersonagemById(1L));
    }

    @Test
    void testCreatePersonagemDeNinjutsu() {
        RequestPersonagemDto dto = RequestPersonagemDto.builder()
                .nome("Sasuke Uchiha").vida(100)
                .jutsus(Map.of("Chidori", 20, "Kirin", 40)).especialidade("ninjutsu").build();

        NinjaDeNinjutsu auxNinja = NinjaDeNinjutsu.builder()
                .nome(dto.nome())
                .vida(dto.vida()).chakra(100).jutsus(dto.jutsus()).build();

        when(personagemRepository.save(auxNinja)).thenReturn(ninjutsu);

        ResponsePersonagemDto result = personagemService.createPersonagem(dto);

        assertEquals("Sasuke Uchiha", result.nome());
        assertEquals("Sasuke Uchiha está atacando com Ninjutsu", ninjutsu.usarJutsu());
        assertEquals("Sasuke Uchiha está desviando usando suas habilidades de Ninjutsu", ninjutsu.desviar());
        assertEquals(100, result.chakra());
        verify(personagemRepository, times(1)).save(auxNinja);
    }

    @Test
    void testCreatePersonagemDeGenjutsu() {
        RequestPersonagemDto dto = RequestPersonagemDto.builder()
                .nome("Itachi Uchiha")
                .vida(100).jutsus(Map.of("Chidori", 20, "Kirin", 40)).especialidade("genjutsu").build();

        NinjaDeGenjutsu auxNinja = NinjaDeGenjutsu.builder()
                .nome(dto.nome())
                .vida(dto.vida()).chakra(100).jutsus(dto.jutsus()).build();

        when(personagemRepository.save(auxNinja)).thenReturn(genjutsu);

        ResponsePersonagemDto result = personagemService.createPersonagem(dto);

        assertEquals("Itachi Uchiha", result.nome());
        assertEquals("Itachi Uchiha está atacando com Genjutsu", genjutsu.usarJutsu());
        assertEquals("Itachi Uchiha está desviando usando suas habilidades de Genjutsu", genjutsu.desviar());
        verify(personagemRepository, times(1)).save(auxNinja);
    }

    @Test
    void testCreatePersonagemDeTaijutsu() {
        RequestPersonagemDto dto = RequestPersonagemDto.builder()
                .nome("Rock Lee")
                .vida(100).jutsus(Map.of("Lotus", 60)).especialidade("taijutsu").build();

        NinjaDeTaijutsu auxNinja = NinjaDeTaijutsu.builder()
                .nome(dto.nome())
                .vida(dto.vida()).chakra(100).jutsus(dto.jutsus()).build();

        when(personagemRepository.save(auxNinja)).thenReturn(taijutsu);

        ResponsePersonagemDto result = personagemService.createPersonagem(dto);

        assertEquals("Rock Lee", result.nome());
        assertEquals("Rock Lee está atacando com Taijutsu", taijutsu.usarJutsu());
        assertEquals("Rock Lee está desviando usando suas habilidades de Taijutsu", taijutsu.desviar());
        verify(personagemRepository, times(1)).save(auxNinja);
    }

    @Test
    void testAprendeJutsu_Success(){
        AprenderJutsuDto dto = AprenderJutsuDto.builder()
                .nomeJutsu("Oodama Rasengan").dano(50).build();

        Map<String, Integer> jutsus = new HashMap<>();

        NinjaDeNinjutsu ninja = NinjaDeNinjutsu.builder()
                        .id(1L).nome("Naruto Uzumaki")
                        .vida(100).chakra(100)
                        .jutsus(jutsus).build();

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninja));
        when(personagemRepository.save(ninja)).thenReturn(ninja);

        ResponsePersonagemDto result = personagemService.aprenderJutsu(1L, dto);

        assertTrue(result.jutsus().containsKey("Oodama Rasengan"));
    }

    @Test
    void testAprendeJutsu_NotFound() {
        AprenderJutsuDto dto = AprenderJutsuDto.builder()
                .nomeJutsu("Oodama Rasengan").dano(50).build();

        when(personagemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> personagemService.aprenderJutsu(1L, dto));
    }

    @Test
    void testAprendeJutsu_BadRequest(){
        AprenderJutsuDto dto = AprenderJutsuDto.builder()
                .nomeJutsu("Oodama Rasengan").build();

        Map<String, Integer> jutsus = new HashMap<>();

        NinjaDeNinjutsu ninja = NinjaDeNinjutsu.builder()
                .id(1L).nome("Naruto Uzumaki")
                .vida(100).chakra(100)
                .jutsus(jutsus).build();

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninja));

        assertThrows(BadRequestException.class, () -> personagemService.aprenderJutsu(1L, dto));
    }

    @Test
    void testCreatePersonagem_InvalidEspecialidade() {
        RequestPersonagemDto dto = RequestPersonagemDto.builder()
                .nome("Sasuke Uchiha")
                .vida(100).jutsus(Map.of("Chidori", 20, "Kirin", 40)).especialidade("abcx").build();

        assertThrows(IllegalArgumentException.class, () -> personagemService.createPersonagem(dto));
    }

    @Test
    void testUpdatePersonagem_Success() {
        RequestPersonagemDto dto = RequestPersonagemDto.builder()
                .nome("Sasuke Uchiha")
                .vida(200).jutsus(Map.of("Chidori", 20, "Kirin", 40)).especialidade("ninjutsu").build();

        NinjaDeNinjutsu auxNinja = NinjaDeNinjutsu.builder()
                .id(1L).nome(dto.nome())
                .vida(dto.vida()).chakra(100).jutsus(dto.jutsus()).build();

        ninjutsu.setVida(dto.vida());

        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));
        when(personagemRepository.save(auxNinja)).thenReturn(ninjutsu);

        ResponsePersonagemDto result = personagemService.updatePersonagem(1L, dto);

        assertEquals(200, result.vida());
        verify(personagemRepository).save(auxNinja);
    }

    @Test
    void testUpdatePersonagem_NotFound() {
        RequestPersonagemDto dto = RequestPersonagemDto.builder()
                .nome("Sasuke Uchiha")
                .vida(200).jutsus(Map.of("Chidori", 20, "Kirin", 40)).especialidade("ninjutsu").build();
        when(personagemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> personagemService.updatePersonagem(1L, dto));
    }

    @Test
    void testDeletePersonagem_Success() {
        when(personagemRepository.findById(1L)).thenReturn(Optional.of(ninjutsu));

        personagemService.deletePersonagem(1L);

        verify(personagemRepository).deleteById(1L);
    }

    @Test
    void testDeletePersonagem_NotFound() {
        when(personagemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> personagemService.deletePersonagem(1L));
    }

}
