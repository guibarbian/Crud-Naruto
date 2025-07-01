package com.db.crud_naruto.DTO.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterDTO(
        @NotNull(message = "O nome é obrigatório")
        @Size(min = 3, max = 20, message = "O nome deve ter entre 3 e 20 caracteres")
        String nome,
        @NotNull(message = "A senha é obrigatória")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
        String senha
){
}
