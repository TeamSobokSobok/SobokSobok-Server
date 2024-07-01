package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SocialSignupRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String socialId,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String username,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String deviceToken,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Platform platform
) {
}
