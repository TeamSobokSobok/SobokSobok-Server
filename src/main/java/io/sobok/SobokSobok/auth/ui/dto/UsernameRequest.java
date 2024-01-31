package io.sobok.SobokSobok.auth.ui.dto;

import jakarta.validation.constraints.NotBlank;

public record UsernameRequest(

        @NotBlank
        String username
) {
}
