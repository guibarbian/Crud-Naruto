package com.db.crud_naruto.controller;

import com.db.crud_naruto.DTO.personagem.AprenderJutsuDto;
import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Personagem", description = "Endpoints para manejo de personagens")
public interface PersonagemController {

    @Operation(summary = "Listar todos os personagens",
            description = "Retorna uma lista paginada de todos os personagens",
            parameters = {
                    @Parameter(name = "page", description = "Número da página", required = false, example = "0"),
                    @Parameter(name = "size", description = "Tamanho da página", required = false, example = "10"),
                    @Parameter(name = "sort", description = "Ordenação (ex: name,age)", required = false, example = "name,asc")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de personagens retornada com sucesso"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping
    Page<ResponsePersonagemDto> findAll(Pageable pageable);

    @Operation(summary = "Listar personagem por ID",
            description = "Retorna um personagem pelo ID",
            parameters = {
                    @Parameter(name = "charId", description = "ID do personagem", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Personagem retornado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/{charId}")
    ResponseEntity<ResponsePersonagemDto> findPersonagemById(@PathVariable Long charId);

    @Operation(summary = "Criar personagem",
            description = "Cria um personagem pelo corpo JSON",
            parameters = {
                    @Parameter(name = "dto", description = "corpo JSON para criação do personagem", required = true, example = "'name:''Rock Lee'")
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro no corpo JSON"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping
    ResponseEntity<ResponsePersonagemDto> createPersonagem(@RequestBody @Valid RequestPersonagemDto dto);

    @Operation(summary = "Atualizar personagem por ID",
            description = "Atualiza um personagem pelo ID",
            parameters = {
                    @Parameter(name = "charId", description = "ID do personagem", required = true, example = "1"),
                    @Parameter(name = "dto", description = "corpo JSON para atualização do personagem", required = true, example = "'name:''Rock Lee'")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Personagem atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro no corpo JSON"),
                    @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping("/{charId}")
    ResponseEntity<ResponsePersonagemDto> updatePersonagem(@PathVariable Long charId, @RequestBody @Valid RequestPersonagemDto dto);

    @Operation(summary = "Aprender jutsu",
            description = "Adiciona um jutsu à lista do personagem",
            parameters = {
                    @Parameter(name = "charId", description = "ID do personagem", required = true, example = "1"),
                    @Parameter(name = "dto", description = "DTO com informações do jutsu", required = true, example = "nomeJutsu: 'Chidori', dano: 100")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Jutsu adicionado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos no body"),
                    @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping("{charId}/aprenderJutsu")
    ResponseEntity<ResponsePersonagemDto> aprenderJutsu(@PathVariable Long charId, @RequestBody @Valid AprenderJutsuDto dto);

    @Operation(summary = "Deletar personagem por ID",
            description = "Deleta um personagem pelo ID",
            parameters = {
                    @Parameter(name = "charId", description = "ID do personagem", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Personagem deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @DeleteMapping("/{charId}")
    ResponseEntity<String> deletePersonagem(@PathVariable Long charId);
}
