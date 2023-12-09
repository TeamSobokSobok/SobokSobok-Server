package io.sobok.SobokSobok.auth.ui.dto;

import lombok.Builder;

@Builder
public record SocialSignupResponse(

        Long id,

        String username,

        String socialId,

        String accessToken,

        String refreshToken
) {
}
