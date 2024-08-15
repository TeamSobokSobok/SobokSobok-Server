package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SocialSignupRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "소셜 아이디가 입력되지 않았습니다.")
        String socialId,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "닉네임이 입력되지 않았습니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2글자 이상 10글자 이하만 가능합니다.")
        String nickname,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "디바이스 토큰이 입력되지 않았습니다.")
        String deviceToken,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "사용자의 모바일 플랫폼이 입력되지 않았습니다.")
        Platform platform
) {
}
