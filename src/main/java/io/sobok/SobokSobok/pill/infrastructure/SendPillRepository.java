package io.sobok.SobokSobok.pill.infrastructure;

import io.sobok.SobokSobok.pill.domain.SendPill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendPillRepository extends JpaRepository<SendPill, Long> {
}
