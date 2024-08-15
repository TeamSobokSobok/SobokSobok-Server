package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.domain.Platform;
import io.sobok.SobokSobok.auth.domain.SocialInfo;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreator {

    private UserRepository userRepository;

    public User create(final String username, final String socialId, final Platform platform, final String deviceToken, final String roles) {
        SocialInfo socialInfo = SocialInfo.newInstance(socialId, platform);
        return User.newInstance(username, socialInfo, deviceToken, roles);
    }
}
