package com.db.crud_naruto.DTO.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
        @NotNull
        String token
) {
}
