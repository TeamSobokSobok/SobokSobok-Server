package io.sobok.SobokSobok.pill.application;

import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PillScheduleServiceUtil {

    public static PillSchedule findPillScheduleById(PillScheduleRepository pillScheduleRepository, Long id) {
        return pillScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.UNREGISTERED_PILL_SCHEDULE));
    }
}
