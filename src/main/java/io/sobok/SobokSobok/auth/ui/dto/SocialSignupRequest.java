package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SocialSignupRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String code,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        SocialType socialType,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String username,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String deviceToken
) {
}
