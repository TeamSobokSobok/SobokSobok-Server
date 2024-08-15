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
    public static SocialSignupResponse of(
            final Long id,
            final String username,
            final String socialId,
            final String accessToken,
            final String refreshToken
    ) {
        return new SocialSignupResponse(id, username, socialId, accessToken, refreshToken);
    }
}
