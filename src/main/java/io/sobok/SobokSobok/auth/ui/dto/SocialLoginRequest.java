package io.sobok.SobokSobok.auth.ui.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SocialLoginRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String socialId,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String deviceToken
) {
}
