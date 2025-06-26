package com.db.crud_naruto.DTO.personagem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Map;

@Builder
public record RequestPersonagemDto(
        @NotNull(message = "Nome é obrigatório")
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
        String nome,

        @NotNull(message = "Vida não pode ser nula")
        @Min(value = 0, message = "Vida deve ser pelo menos 0")
        @Max(value = 500, message = "Vida deve ser no máximo 500")
        Integer vida,

        @NotNull(message = "Chakra não pode ser nula")
        @Min(value = 0, message = "Chakra deve ser pelo menos 0")
        @Max(value = 500, message = "Chakra deve ser no máximo 500")
        Integer chakra,

        @NotNull(message = "A lista de jutsus não pode ser nula")
        @NotEmpty(message = "A lista de jutsus não pode estar vazia")
        Map<String, Integer> jutsus,

        @NotNull(message = "Especialidade é obrigatória")
        String especialidade
) {
}
