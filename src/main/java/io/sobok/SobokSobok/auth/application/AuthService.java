package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.auth.ui.dto.JwtTokenResponse;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.security.jwt.Jwt;
import io.sobok.SobokSobok.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProvider jwtProvider;

    @Transactional
    public JwtTokenResponse refresh(String refresh) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String socialId = valueOperations.get(refresh);

        if (socialId == null) {
            throw new NotFoundException(ErrorCode.UNREGISTERED_TOKEN);
        }

        if(!userRepository.existsBySocialInfoSocialId(socialId)) {
            throw new NotFoundException(ErrorCode.UNREGISTERED_USER);
        }

        Jwt jwt = jwtProvider.getUserJwt(socialId);

        return JwtTokenResponse.builder()
                .accessToken(jwt.accessToken())
                .refreshToken(jwt.refreshToken())
                .build();
    }
}
