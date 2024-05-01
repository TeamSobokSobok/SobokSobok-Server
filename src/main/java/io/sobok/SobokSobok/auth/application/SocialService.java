package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.domain.Role;
import io.sobok.SobokSobok.auth.domain.SocialInfo;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginResponse;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupResponse;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.security.jwt.Jwt;
import io.sobok.SobokSobok.security.jwt.JwtProvider;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    @Transactional
    public SocialSignupResponse signup(SocialSignupRequest request) {
        if (userRepository.existsBySocialInfoSocialId(request.socialId())) {
            throw new ConflictException(ErrorCode.ALREADY_EXISTS_USER);
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException(ErrorCode.ALREADY_USING_USERNAME);
        }

        User signupUser = userRepository.save(User.builder()
                .username(request.username())
                .socialInfo(SocialInfo.builder()
                        .socialId(request.socialId())
                        .build())
                .deviceToken(request.deviceToken())
                .roles(Role.USER.name())
                .build());

        Jwt jwt = jwtProvider.getUserJwt(signupUser.getSocialInfo().getSocialId());

        return SocialSignupResponse.builder()
            .id(signupUser.getId())
            .username(signupUser.getUsername())
            .socialId(signupUser.getSocialInfo().getSocialId())
            .accessToken(jwt.accessToken())
            .refreshToken(jwt.refreshToken())
            .build();
    }

    @Transactional
    public SocialLoginResponse login(SocialLoginRequest request) {
        Optional<User> optionalLoginUser = userRepository.findBySocialInfoSocialId(
                request.socialId());

        if (optionalLoginUser.isPresent()) {
            User loginUser = optionalLoginUser.get();

            if (!request.deviceToken().equals(loginUser.getDeviceToken())) {
                loginUser.updateDeviceToken(request.deviceToken());
            }

            Jwt jwt = jwtProvider.getUserJwt(loginUser.getSocialInfo().getSocialId());

            return SocialLoginResponse.builder()
                    .accessToken(jwt.accessToken())
                    .refreshToken(jwt.refreshToken())
                    .isNew(false)
                    .build();
        } else {
            return SocialLoginResponse.builder()
                    .isNew(true)
                    .build();
        }
    }
}
