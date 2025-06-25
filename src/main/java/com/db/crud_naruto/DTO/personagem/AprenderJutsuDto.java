package com.db.crud_naruto.DTO.personagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
public record AprenderJutsuDto(
        @NotBlank(message = "Nome do jutsu é obrigatório")
        String nomeJutsu,

        @NotNull(message = "Dano do Jutsu não pode ser nulo")
        Integer dano
) {
}
