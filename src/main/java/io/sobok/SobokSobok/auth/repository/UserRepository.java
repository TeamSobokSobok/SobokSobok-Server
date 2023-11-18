package io.sobok.SobokSobok.auth.repository;

import io.sobok.SobokSobok.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
