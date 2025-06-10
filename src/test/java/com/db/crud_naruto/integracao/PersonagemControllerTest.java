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
    private String baseUrl = "/api/v2/personagens";

    @Autowired
    private PersonagemRepository personagemRepository;

    @BeforeEach
    void setUp() throws Exception {

        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("Paul")
                .senha("123456")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));

        AuthenticationDTO loginDTO = AuthenticationDTO.builder()
                .nome("Paul")
                .senha("123456")
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

        mockMvc.perform(get(baseUrl + "/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                .andExpect(jsonPath("$.chakra").value(100))
                .andExpect(jsonPath("$.jutsus.['Rasengan']").value(20));
    }

    @Test
    void testFindAllPersonagens() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .header("Authorization", token)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Sasuke Uchiha"))
                .andExpect(jsonPath(("$.content[1].nome")).value("Rock Lee"));
    }


    @Test
    void testCreatePersonagem() throws Exception {
        RequestPersonagemDto personagem = RequestPersonagemDto.builder()
                .nome("Kakashi Hatake")
                .vida(100)
                .chakra(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Raikiri", 20, "Kamui", 20))
                .build();

        mockMvc.perform(post(baseUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personagem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Kakashi Hatake"))
                .andExpect(jsonPath("$.chakra").value(100));
    }

    @Test
    void testEspecialidadeInvalida() throws Exception{
        RequestPersonagemDto personagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .vida(100)
                .especialidade("NoJutsu")
                .jutsus(Map.of("Rasengan", 20, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(post(baseUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personagem)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNotFoundUpdate() throws Exception{
        RequestPersonagemDto personagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .vida(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put(baseUrl + "/99999")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personagem)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEspecialidadeInvalidaUpdate() throws Exception{
        RequestPersonagemDto updateNinja = RequestPersonagemDto.builder()
                        .nome("Naruto Uzumaki")
                        .vida(120)
                        .chakra(120)
                        .especialidade("NoJutsu")
                        .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20)).build();

        mockMvc.perform(put(baseUrl + "/10")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNinja)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePersonagem() throws Exception {
        NinjaDeNinjutsu ninja = NinjaDeNinjutsu.builder()
                .nome("Tobirama Senju")
                .chakra(100)
                .vida(100)
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        NinjaDeNinjutsu savedNinja = personagemRepository.save(ninja);

        RequestPersonagemDto updatedPersonagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .vida(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put(baseUrl + "/" + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonagem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jutsus['Rasengan']").value(40));

        personagemRepository.deleteById(savedNinja.getId());
    }

    @Test
    void testDeletePersonagem() throws Exception {
        NinjaDeNinjutsu ninja = NinjaDeNinjutsu.builder()
                .nome("Naruto Uzumaki")
                .vida(100)
                .chakra(100)
                .jutsus(Map.of("Rasengan", 40, "Kage Bushin no Jutsu", 20))
                .build();

        NinjaDeNinjutsu savedNinja = personagemRepository.save(ninja);

        mockMvc.perform(delete(baseUrl + "/" + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get(baseUrl + savedNinja.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
