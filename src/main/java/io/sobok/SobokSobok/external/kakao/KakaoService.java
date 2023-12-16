package io.sobok.SobokSobok.external.kakao;

import io.sobok.SobokSobok.external.kakao.dto.response.KakaoProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoUserClient kakaoUserClient;

    @Value("${kakao.grant-type}")
    private String grantType;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    public KakaoProfile getProfileByCode(String code) {
        String accessToken = getAccessToken(code);
        return kakaoUserClient.getProfile("Bearer " + accessToken);
    }

    private String getAccessToken(String code) {
        return kakaoAuthClient.getAccessToken(
                grantType,
                clientId,
                redirectUri,
                code,
                clientSecret
        ).accessToken();
    }
}
