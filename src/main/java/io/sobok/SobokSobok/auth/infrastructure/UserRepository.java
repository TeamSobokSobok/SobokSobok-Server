package io.sobok.SobokSobok.auth.infrastructure;

import io.sobok.SobokSobok.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // READ
    Optional<User> findBySocialInfoSocialId(String socialId);

    Boolean existsBySocialInfoSocialId(String socialId);

    Boolean existsByUsername(String username);
}
