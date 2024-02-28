package io.sobok.SobokSobok.pill.infrastructure;

import io.sobok.SobokSobok.pill.domain.PillSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PillScheduleRepository extends JpaRepository<PillSchedule, Long> {

    // CREATE

    // READ
    List<PillSchedule> findAllByScheduleDateAndScheduleTime(LocalDate scheduleDate, String scheduleTime);

    // UPDATE

    // DELETE
    void deleteAllByPillId(Long pillId);
}
