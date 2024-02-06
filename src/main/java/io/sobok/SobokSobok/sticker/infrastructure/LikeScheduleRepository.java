package io.sobok.SobokSobok.sticker.infrastructure;

import io.sobok.SobokSobok.sticker.domain.LikeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeScheduleRepository extends JpaRepository<LikeSchedule, Long> {

    Boolean existsBySenderIdAndScheduleId(Long senderId, Long scheduleId);
}
