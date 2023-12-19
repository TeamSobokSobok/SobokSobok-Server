package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SocialLoginRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String code,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        SocialType socialType,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String deviceToken
) {
}
