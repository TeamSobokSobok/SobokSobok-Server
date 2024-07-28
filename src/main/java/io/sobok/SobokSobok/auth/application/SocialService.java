package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.Role;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginResponse;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupResponse;
import io.sobok.SobokSobok.security.jwt.Jwt;
import io.sobok.SobokSobok.security.jwt.JwtProvider;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final UserCreator userCreator;

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    @Transactional
    public SocialSignupResponse signup(SocialSignupRequest request) {
        UserServiceUtil.checkAlreadySignupSocialId(userRepository, request.socialId());
        UserServiceUtil.checkAlreadyUsedNickname(userRepository, request.nickname());

        User signupUser = userCreator.create(
                request.nickname(),
                request.socialId(),
                request.platform(),
                request.deviceToken(),
                Role.USER.name()
        );

        Jwt jwt = jwtProvider.getUserJwt(signupUser.getSocialInfo().getSocialId());

        return SocialSignupResponse.of(
                signupUser.getId(),
                signupUser.getUsername(),
                signupUser.getSocialInfo().getSocialId(),
                jwt.accessToken(),
                jwt.refreshToken()
        );
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

            if (!request.platform().equals(loginUser.getSocialInfo().getPlatform())) {
                loginUser.updatePlatform(request.platform());
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
