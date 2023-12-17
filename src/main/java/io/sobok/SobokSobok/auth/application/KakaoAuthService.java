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
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.external.kakao.KakaoService;
import io.sobok.SobokSobok.external.kakao.dto.response.KakaoProfile;
import io.sobok.SobokSobok.security.jwt.Jwt;
import io.sobok.SobokSobok.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoAuthService extends AuthService {

    private final KakaoService kakaoService;

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    @Transactional
    public SocialSignupResponse signup(SocialSignupRequest request) {
        KakaoProfile kakaoProfile = kakaoService.getProfileByCode(request.code());

        if (userRepository.existsBySocialInfoSocialId(kakaoProfile.id())) {
            throw new ConflictException(ErrorCode.ALREADY_EXISTS_USER);
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException(ErrorCode.ALREADY_USING_USERNAME);
        }

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

        Jwt jwt = getUserJwt(signupUser.getUsername());

        return SocialSignupResponse.builder()
                .id(signupUser.getId())
                .username(signupUser.getUsername())
                .socialId(signupUser.getSocialInfo().getSocialId())
                .accessToken(jwt.accessToken())
                .refreshToken(jwt.refreshToken())
                .build();
    }

    @Override
    @Transactional
    public SocialLoginResponse login(SocialLoginRequest request) {
        KakaoProfile kakaoProfile = kakaoService.getProfileByCode(request.code());

        User loginUser = userRepository.findBySocialInfoSocialId(kakaoProfile.id())
                .orElseThrow(() -> new NotFoundException(ErrorCode.UNREGISTERED_USER));

       if (!request.deviceToken().equals(loginUser.getDeviceToken())) {
           loginUser.updateDeviceToken(request.deviceToken());
       }

       Jwt jwt = getUserJwt(loginUser.getUsername());

        return SocialLoginResponse.builder()
                .accessToken(jwt.accessToken())
                .refreshToken(jwt.refreshToken())
                .build();
    }

    private Jwt getUserJwt(String principle) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principle, null, List.of(new SimpleGrantedAuthority(Role.USER.name())));
        return jwtProvider.createToken(authenticationToken);
    }
}