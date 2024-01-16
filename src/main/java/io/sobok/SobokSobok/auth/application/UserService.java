package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.auth.ui.dto.UsernameResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                .deviceOS(member.getSocialInfo().getSocialType())
                .selfCheck(member.getId().equals(userId))
                .build())
        );

        return result;
    }
}
