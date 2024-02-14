package io.sobok.SobokSobok.auth.application.util;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUtil {

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
