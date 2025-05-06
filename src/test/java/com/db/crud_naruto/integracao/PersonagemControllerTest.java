package com.db.crud_naruto.integracao;

import com.db.crud_naruto.DTO.auth.AuthenticationDTO;
import com.db.crud_naruto.DTO.auth.RegisterDTO;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.model.NinjaDeGenjutsu;
import com.db.crud_naruto.model.NinjaDeNinjutsu;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String token;
    @Autowired
    private PersonagemRepository personagemRepository;

    @BeforeEach
    void setUp() throws Exception {
        usuarioRepository.deleteAll();
        personagemRepository.deleteAll();

        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("kakashi")
                .senha("hatake123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));

        AuthenticationDTO loginDTO = AuthenticationDTO.builder()
                .nome("kakashi")
                .senha("hatake123")
                .build();

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        token = "Bearer " + objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void testFindPersonagemById() throws Exception{
        NinjaDeNinjutsu ninja = NinjaDeNinjutsu.builder()
                .nome("Sasuke Uchiha").idade(17)
                .aldeia("Renegado").vida(100).chakra(100)
                .jutsus(Map.of("Chidori", 20, "Kirin", 40)).build();

        NinjaDeNinjutsu savedNinja = personagemRepository.save(ninja);

        mockMvc.perform(get("/api/v1/personagens/" + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedNinja)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sasuke Uchiha"))
                .andExpect(jsonPath("$.aldeia").value("Renegado"))
                .andExpect(jsonPath("$.chakra").value(100))
                .andExpect(jsonPath("$.jutsus.['Chidori']").value(20));
    }

    @Test
    void testFindAllPersonagens() throws Exception {
        NinjaDeNinjutsu ninja1 = NinjaDeNinjutsu.builder()
                .nome("Sasuke Uchiha").idade(17)
                .aldeia("Renegado").vida(100).chakra(100)
                .jutsus(Map.of("Chidori", 20, "Kirin", 40)).build();

        personagemRepository.save(ninja1);

        NinjaDeGenjutsu ninja2 = NinjaDeGenjutsu.builder()
                .nome("Itachi Uchiha").idade(22)
                .aldeia("Akatsuki").chakra(100).vida(100)
                .jutsus(Map.of("Tsukuyomi", 40, "Shishirendan", 20)).build();

        personagemRepository.save(ninja2);

        mockMvc.perform(get("/api/v1/personagens")
                        .header("Authorization", token)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Sasuke Uchiha"))
                .andExpect(jsonPath(("$.content[1].nome")).value("Itachi Uchiha"));
    }


    @Test
    void testCreatePersonagem() throws Exception {
        RequestPersonagemDto personagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .idade(16)
                .aldeia("Aldeia da Folha")
                .vida(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(post("/api/v1/personagens")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personagem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                .andExpect(jsonPath("$.aldeia").value("Aldeia da Folha"))
                .andExpect(jsonPath("$.chakra").value(100));
    }

    @Test
    void testEspecialidadeInvalida() throws Exception{
        RequestPersonagemDto personagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .idade(16)
                .aldeia("Aldeia da Folha")
                .vida(100)
                .especialidade("NoJutsu")
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(post("/api/v1/personagens")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personagem)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNotFoundUpdate() throws Exception{
        RequestPersonagemDto personagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .idade(16)
                .aldeia("Aldeia da Folha")
                .vida(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put("/api/v1/personagens/9999")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personagem)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEspecialidadeInvalidaUpdate() throws Exception{
        NinjaDeNinjutsu personagem = NinjaDeNinjutsu.builder()
                .nome("Naruto Uzumaki")
                .idade(16)
                .aldeia("Aldeia da Folha")
                .vida(100)
                .chakra(100)
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        NinjaDeNinjutsu savedNinja = personagemRepository.save(personagem);

        RequestPersonagemDto updateNinja = RequestPersonagemDto.builder()
                        .nome("Naruto Uzumaki")
                        .idade(17)
                        .aldeia("Aldeia da Folha")
                        .vida(120)
                        .especialidade("NoJutsu")
                        .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20)).build();

        mockMvc.perform(put("/api/v1/personagens/" + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNinja)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePersonagem() throws Exception {
        NinjaDeNinjutsu ninja = NinjaDeNinjutsu.builder()
                .nome("Naruto Uzumaki")
                .idade(16)
                .aldeia("Aldeia da Folha")
                .chakra(100)
                .vida(100)
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        NinjaDeNinjutsu savedNinja = personagemRepository.save(ninja);

        RequestPersonagemDto updatedPersonagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .idade(17)
                .vida(100)
                .aldeia("Aldeia da Folha")
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put("/api/v1/personagens/" + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonagem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idade").value(17))
                .andExpect(jsonPath("$.jutsus['Rasengan']").value(40));
    }

    @Test
    void testDeletePersonagem() throws Exception {
        NinjaDeNinjutsu ninja = NinjaDeNinjutsu.builder()
                .nome("Naruto Uzumaki")
                .idade(16)
                .aldeia("Aldeia da Folha")
                .vida(100)
                .chakra(100)
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        NinjaDeNinjutsu savedNinja = personagemRepository.save(ninja);

        mockMvc.perform(delete("/api/v1/personagens/" + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/personagens/" + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
