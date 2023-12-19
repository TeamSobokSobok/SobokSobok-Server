package io.sobok.SobokSobok.auth.ui.dto;

import lombok.Builder;

@Builder
public record JwtTokenResponse(

        String accessToken,

        String refreshToken
) { }
