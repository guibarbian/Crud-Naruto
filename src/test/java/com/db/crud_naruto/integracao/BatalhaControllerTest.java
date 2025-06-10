package com.db.crud_naruto.integracao;

import com.db.crud_naruto.DTO.auth.AuthenticationDTO;
import com.db.crud_naruto.DTO.auth.RegisterDTO;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.repository.PersonagemRepository;
import com.db.crud_naruto.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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
public class BatalhaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String token;
    private String baseUrl = "/api/v2/batalhas";

    @Autowired
    private PersonagemRepository personagemRepository;

    @BeforeEach
    void setUp() throws Exception{
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
    void testBatalhaVencidaPorVida() throws Exception {
        RequestPersonagemDto updatedPersonagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .vida(0)
                .chakra(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 20, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put("/api/v2/personagens/1")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonagem)))
                        .andExpect(status().isOk());

        mockMvc.perform(get(baseUrl + "/" + 1 + "/" + 2)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Fim da batalha! Sasuke Uchiha venceu!"));

        RequestPersonagemDto updatedPersonagem2 = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .vida(100)
                .chakra(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 20, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put("/api/v2/personagens/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonagem2)))
                .andExpect(status().isOk());
    }

    @Test
    void testBatalhaVencidaPorChakra() throws Exception {
        RequestPersonagemDto updatedPersonagem = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .vida(100)
                .chakra(0)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 20, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put("/api/v2/personagens/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonagem)))
                .andExpect(status().isOk());

        mockMvc.perform(get(baseUrl + "/" + 1 + "/" + 2)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Fim da batalha! Sasuke Uchiha venceu!"));

        RequestPersonagemDto updatedPersonagem2 = RequestPersonagemDto.builder()
                .nome("Naruto Uzumaki")
                .vida(100)
                .chakra(100)
                .especialidade("Ninjutsu")
                .jutsus(Map.of("Rasengan", 20, "Kage Bushin no Jutsu", 20))
                .build();

        mockMvc.perform(put("/api/v2/personagens/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonagem2)))
                .andExpect(status().isOk());
    }

    @Test
    public void testBatalhaNotFound() throws Exception{
        mockMvc.perform(get(baseUrl + "/" + 9999 + "/" + 99999)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Personagem não encontrado"));
    }
}
