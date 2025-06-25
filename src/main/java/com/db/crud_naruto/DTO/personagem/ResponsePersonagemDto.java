package com.db.crud_naruto.DTO.personagem;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Map;

@Builder
public record ResponsePersonagemDto(
        Long id,
        String nome,
        Integer vida,
        Integer chakra,
        Map<String, Integer> jutsus
) {
}
