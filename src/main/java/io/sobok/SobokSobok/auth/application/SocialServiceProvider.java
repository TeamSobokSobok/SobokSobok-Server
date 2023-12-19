package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.domain.SocialType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class SocialServiceProvider {

    private static final Map<SocialType, SocialService> socialServiceMap = new HashMap<>();

    private final KakaoSocialService kakaoSocialService;

    @PostConstruct
    void initializeSocialServiceMap() {
        socialServiceMap.put(SocialType.KAKAO, kakaoSocialService);
    }

    public SocialService getSocialService(SocialType socialType) {
        return socialServiceMap.get(socialType);
    }
}
