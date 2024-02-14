package io.sobok.SobokSobok.pill.infrastructure;

import io.sobok.SobokSobok.pill.domain.SendPill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SendPillRepository extends JpaRepository<SendPill, Long> {

    Optional<SendPill> findByPillId(Long pillId);
}
