package io.sobok.SobokSobok.auth.ui.dto;

import lombok.Builder;

@Builder
public record SocialLoginResponse(

        String accessToken,
        String refreshToken,
        Boolean isNew
) {
}
