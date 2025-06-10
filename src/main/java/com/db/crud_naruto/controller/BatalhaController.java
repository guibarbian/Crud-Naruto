package com.db.crud_naruto.controller;

import com.db.crud_naruto.DTO.personagem.RequestPersonagemDto;
import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Batalha", description = "Endpoints para batalha")
public interface BatalhaController {

    @Operation(summary = "Realizar Luta",
            description = "Incia uma luta entre dois personagens",
            parameters = {
                    @Parameter(name = "idNinja1", description = "ID do personagem 1", required = true, example = "1"),
                    @Parameter(name = "idNinja2", description = "ID do personagem 2", required = true, example = "2")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Batalha realizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    ResponseEntity<String> iniciarBatalha(Long ninja1, Long ninja2);
}
