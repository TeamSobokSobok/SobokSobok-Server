package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.domain.SocialType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthServiceProvider {

    private static final Map<SocialType, AuthService> authServiceMap = new HashMap<>();

    private final KakaoAuthService kakaoAuthService;

    @PostConstruct
    void initializeAuthServiceMap() {
        authServiceMap.put(SocialType.KAKAO, kakaoAuthService);
    }

    public AuthService getAuthService(SocialType socialType) {
        return authServiceMap.get(socialType);
    }
}
