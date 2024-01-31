package io.sobok.SobokSobok.pill.infrastructure;

import io.sobok.SobokSobok.pill.domain.Pill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PillRepository extends JpaRepository<Pill, Long> {

    // READ
    List<Pill> findAllByUserId(Long userId);
}
