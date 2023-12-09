package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SocialSignupRequest(

        @Schema(required = true)
        @NotBlank
        String code,

        @Schema(required = true)
        @NotNull
        SocialType socialType,

        @Schema(required = true)
        @NotBlank
        String username,

        @Schema(required = true)
        @NotBlank
        String deviceToken
) {
}
