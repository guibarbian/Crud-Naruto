package com.db.crud_naruto.DTO.personagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
public record AprenderJutsuDto(
        String nomeJutsu,
        Integer dano
) {
}
