package io.sobok.SobokSobok.pill.infrastructure;

import io.sobok.SobokSobok.pill.domain.PillSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PillScheduleRepository extends JpaRepository<PillSchedule, Long> {

    void deleteAllByPillId(Long pillId);
}
