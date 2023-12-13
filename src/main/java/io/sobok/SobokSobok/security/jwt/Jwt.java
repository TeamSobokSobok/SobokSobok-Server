package io.sobok.SobokSobok.security.jwt;

import lombok.Builder;

@Builder
public record Jwt(
        String accessToken,
        String refreshToken
) { }
