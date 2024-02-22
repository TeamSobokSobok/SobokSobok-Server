package io.sobok.SobokSobok.auth.ui.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SocialSignupRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String socialId,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String username,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String deviceToken
) {
}
