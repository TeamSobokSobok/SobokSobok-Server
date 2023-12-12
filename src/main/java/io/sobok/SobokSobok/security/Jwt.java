package io.sobok.SobokSobok.security;

import lombok.Builder;

@Builder
public record Jwt(
        String accessToken,
        String refreshToken
) { }
