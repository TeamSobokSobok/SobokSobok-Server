package io.sobok.SobokSobok.auth.application.util;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUtil {

    public static void checkAlreadySignupSocialId(UserRepository userRepository, String socialId) {
        if (userRepository.existsBySocialInfoSocialId(socialId)) {
            throw new ConflictException(ErrorCode.ALREADY_EXISTS_USER);
        }
    }

    public static void checkAlreadyUsedNickname(UserRepository userRepository, String nickname) {
        if (userRepository.existsByUsername(nickname)) {
            throw new ConflictException(ErrorCode.ALREADY_USING_NICKNAME);
        }
    }

    public static User findUserById(UserRepository userRepository, Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.UNREGISTERED_USER));
    }

    public static void existsUserById(UserRepository userRepository, Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.UNREGISTERED_USER);
        }
    }
}
