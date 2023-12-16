package io.sobok.SobokSobok.external.kakao;

import io.sobok.SobokSobok.external.kakao.dto.response.KakaoProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoUserClient", url = "https://kapi.kakao.com/v2")
public interface KakaoUserClient {

    @GetMapping("/user/me")
    KakaoProfile getProfile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
    );
}
