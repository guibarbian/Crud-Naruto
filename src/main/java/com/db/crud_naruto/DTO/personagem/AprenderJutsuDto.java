package com.db.crud_naruto.DTO.personagem;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
public record AprenderJutsuDto(
        @NotBlank(message = "Nome do jutsu é obrigatório")
        @Size(min = 5, max = 50, message = "O nome do Jutsu deve ter entre 5 e 50 caracteres")
        String nomeJutsu,

        @NotNull(message = "Dano do Jutsu não pode ser nulo")
        @Min(value = 1, message = "O dano do jutsu deve ser entre 1 e 120")
        @Max(value = 120, message = "O dano do jutsu deve ser entre 1 e 120")
        Integer dano
) {
}
