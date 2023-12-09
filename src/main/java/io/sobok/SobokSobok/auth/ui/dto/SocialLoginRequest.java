package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SocialLoginRequest(

        @Schema(required = true)
        String code,

        @Schema(required = true)
        @NotNull
        SocialType socialType,

        @Schema(required = true)
        String deviceToken
) {
}
