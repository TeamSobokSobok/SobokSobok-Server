package io.sobok.SobokSobok.pill.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.pill.ui.dto.CheckPillScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PillScheduleService {

    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final PillScheduleRepository pillScheduleRepository;

    @Transactional
    public CheckPillScheduleResponse changePillScheduleCheck(Long userId, Long scheduleId, Boolean isCheck) {
        UserServiceUtil.existsUserById(userRepository, userId);

        PillSchedule pillSchedule = PillScheduleServiceUtil.findPillScheduleById(pillScheduleRepository, scheduleId);

        Pill pill = PillServiceUtil.findPillById(pillRepository, pillSchedule.getPillId());

        if (!pill.getUserId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        pillSchedule.changePillScheduleCheck(isCheck);

        return CheckPillScheduleResponse.builder()
            .scheduleId(scheduleId)
            .pillId(pill.getId())
            .userId(userId)
            .scheduleDate(pillSchedule.getScheduleDate().atStartOfDay())
            .scheduleTime(pillSchedule.getScheduleTime())
            .isCheck(isCheck)
            .build();
    }
}
