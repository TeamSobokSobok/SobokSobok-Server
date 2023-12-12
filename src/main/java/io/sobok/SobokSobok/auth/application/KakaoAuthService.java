package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.domain.Role;
import io.sobok.SobokSobok.auth.domain.SocialInfo;
import io.sobok.SobokSobok.auth.domain.SocialType;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginResponse;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupResponse;
import io.sobok.SobokSobok.external.kakao.KakaoService;
import io.sobok.SobokSobok.external.kakao.dto.response.KakaoProfile;
import io.sobok.SobokSobok.security.Jwt;
import io.sobok.SobokSobok.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoAuthService extends AuthService {

    private final KakaoService kakaoService;

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public SocialSignupResponse signup(SocialSignupRequest request) {
        KakaoProfile kakaoProfile = kakaoService.getProfileByCode(request.code());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoProfile.id(), "");

        User signupUser = userRepository.save(User.builder()
                .username(request.username())
                .socialInfo(SocialInfo.builder()
                        .socialType(SocialType.KAKAO)
                        .socialId(kakaoProfile.id())
                        .socialProfileImage(kakaoProfile.kakaoAccount().profile().profileImageUrl())
                        .build())
                .deviceToken(request.deviceToken())
                .roles(Role.USER.name())
                .build());

        // JWT 발급

        return SocialSignupResponse.builder()
                .id(signupUser.getId())
                .username(signupUser.getUsername())
                .socialId(signupUser.getSocialInfo().getSocialId())
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    @Override
    @Transactional
    public SocialLoginResponse login(SocialLoginRequest request) {
        KakaoProfile kakaoProfile = kakaoService.getProfileByCode(request.code());

        User loginUser = userRepository.findBySocialInfoSocialId(kakaoProfile.id())
                .orElseThrow(() -> new RuntimeException("Error"));

        // deviceToken 업데이트
       if (!request.deviceToken().equals(loginUser.getDeviceToken())) {
           loginUser.updateDeviceToken(request.deviceToken());
       }

        // JWT 발급

        return SocialLoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }
}
