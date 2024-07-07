package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SocialLoginRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String socialId,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String deviceToken,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Platform platform
) {
}
