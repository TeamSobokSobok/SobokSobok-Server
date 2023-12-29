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

        validatePillCount(user.getId(), request.pillName().length);
        validatePillRequest(request.startDate(), request.endDate(), request.day(), request.timeList());

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

    @Transactional
    public Integer getPillCount(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(ErrorCode.UNREGISTERED_USER);
        }

        return pillQueryRepository.getPillCount(userId);
    }

    private void validatePillCount(Long userId, int requestPillCount) {
        if (pillQueryRepository.getPillCount(userId) + requestPillCount > 5) {
            throw new BadRequestException(ErrorCode.EXCEEDED_PILL_COUNT);
        }
    }

    private void validatePillRequest(LocalDate startDate, LocalDate endDate, String day, String[] timeList) {
        if (startDate.isAfter(endDate) || day.isEmpty() || timeList.length == 0) {
            throw new BadRequestException(ErrorCode.INVALID_PILL_REQUEST_DATA);
        }
    }
}
