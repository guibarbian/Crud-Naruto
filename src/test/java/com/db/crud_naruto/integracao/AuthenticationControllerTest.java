package com.db.crud_naruto.integracao;

import com.db.crud_naruto.DTO.auth.AuthenticationDTO;
import com.db.crud_naruto.DTO.auth.RegisterDTO;
import com.db.crud_naruto.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("naruto")
                .senha("ramen123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void deveAutenticarUsuarioComSucesso() throws Exception {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("Naruto")
                .senha("Ramen123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        AuthenticationDTO authDTO = AuthenticationDTO.builder()
                .nome("Naruto")
                .senha("Ramen123")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void deveRetornarErroAoRegistrarUsuarioExistente() throws Exception {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("sakura")
                .senha("sakura123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroAoAutenticarUsuarioInexistente() throws Exception {
        AuthenticationDTO authDTO = AuthenticationDTO.builder()
                .nome("itachi")
                .senha("sharingan123")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Não existe usuário com esse nome"));
    }

    @Test
    void deveRetornar400AoLogarComSenhaErrada() throws Exception{
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("sasuke")
                .senha("chidori123").build();

        AuthenticationDTO authDTO = AuthenticationDTO.builder()
                .nome("sasuke")
                .senha("chidori122").build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Senha incorreta"));
    }

    @Test
    void deveRetornarErroAoRegistrarUsuarioComNomeNulo() throws Exception{
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome(null)
                .senha("Ramen123").build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.registerDTO").value("O nome é obrigatório"));
    }

    @Test
    void deveRetornarErroAoRegistrarUsuarioComSenhaCurta() throws Exception{
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("naruto")
                .senha("123").build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.registerDTO").value("A senha deve ter entre 6 e 20 caracteres"));
    }

    @Test
    void testRegistrarUsuarioComNomeCurto() throws Exception{
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("sa")
                .senha("sakura123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.registerDTO").value("O nome deve ter entre 3 e 20 caracteres"));
    }

    @Test
    void testRegistrarUsuarioComSenhaCurta() throws Exception{
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("Sasuke")
                .senha("sa")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.registerDTO").value("A senha deve ter entre 6 e 20 caracteres"));
    }

    @Test
    void testRegistrarUsuarioComNomeNulo() throws Exception{
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome(null)
                .senha("sakura123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.registerDTO").value("O nome é obrigatório"));
    }

    @Test
    void testRegistrarUsuarioComSenhaNulo() throws Exception{
        RegisterDTO registerDTO = RegisterDTO.builder()
                .nome("Sasuke")
                .senha(null)
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.registerDTO").value("A senha é obrigatória"));
    }
}
