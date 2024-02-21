package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.auth.ui.dto.UsernameResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Boolean duplicateNickname(String username) {

        return userRepository.existsByUsername(username);
    }

    @Transactional
    public List<UsernameResponse> getUsername(Long userId, String username) {
        UserServiceUtil.existsUserById(userRepository, userId);

        Optional<User> optionalMember = userRepository.findByUsername(username);

        List<UsernameResponse> result = new ArrayList<>();

        optionalMember.ifPresent(
            member -> result.add(UsernameResponse.builder()
                .memberId(member.getId())
                .memberName(member.getUsername())
                .selfCheck(member.getId().equals(userId))
                .build())
        );

        return result;
    }

    @Transactional
    public void changeUsername(Long userId, String username) {

        User user = UserServiceUtil.findUserById(userRepository, userId);

        if (duplicateNickname(username)) {
            throw new ConflictException(ErrorCode.ALREADY_USING_USERNAME);
        }

        user.changeUsername(username);
    }
}
