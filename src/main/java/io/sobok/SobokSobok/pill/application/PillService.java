package io.sobok.SobokSobok.pill.application;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.infrastructure.PillQueryRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.pill.ui.dto.PillRequest;
import io.sobok.SobokSobok.utils.PillUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PillService {

    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final PillQueryRepository pillQueryRepository;
    private final PillScheduleRepository pillScheduleRepository;

    @Transactional
    public void addPill(Long userId, PillRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.UNREGISTERED_USER));

        validatePillCount(user.getId());

        for (String pill : request.pillName()) {
            Pill newPill = pillRepository.save(Pill.builder()
                    .pillName(pill)
                    .color(PillUtil.getRandomColorNumber())
                    .startDate(request.startDate())
                    .endDate(request.endDate())
                    .scheduleDay(request.day())
                    .userId(user.getId())
                    .build()
            );

            LocalDate[] scheduleDate = PillUtil.getScheduleDateList(request.startDate(), request.endDate(), request.day().split(", "));
            for (LocalDate date : scheduleDate) {
                for (String time : request.timeList()) {
                    pillScheduleRepository.save(PillSchedule.builder()
                            .scheduleDate(date)
                            .scheduleTime(time)
                            .pillId(newPill.getId())
                            .build()
                    );
                }
            }
        }
    }

    private void validatePillCount(Long userId) {
        if (pillQueryRepository.getPillCount(userId) >= 5) {
            throw new BadRequestException(ErrorCode.EXCEEDED_PILL_COUNT);
        }
    }
}
