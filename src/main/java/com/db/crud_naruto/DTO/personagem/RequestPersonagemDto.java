package com.db.crud_naruto.DTO.personagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
public record RequestPersonagemDto(
        String nome,
        Integer vida,
        Integer chakra,
        Map<String, Integer> jutsus,
        String especialidade
) {
}
