package io.sobok.SobokSobok.pill.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.friend.infrastructure.FriendQueryRepository;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleQueryRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.pill.ui.dto.CheckPillScheduleResponse;
import io.sobok.SobokSobok.pill.ui.dto.DateScheduleResponse;
import io.sobok.SobokSobok.pill.ui.dto.MonthScheduleResponse;
import io.sobok.SobokSobok.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PillScheduleService {

    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final PillScheduleRepository pillScheduleRepository;
    private final PillScheduleQueryRepository pillScheduleQueryRepository;
    private final FriendQueryRepository friendQueryRepository;

    @Transactional
    public List<MonthScheduleResponse> getMonthSchedule(Long userId, LocalDate date) {
        UserServiceUtil.existsUserById(userRepository, userId);

        LocalDate startDateOfMonth = DateUtil.getStartDateOfMonth(date);
        LocalDate endDateOfMonth = DateUtil.getEndDateOfMonth(date);

        return pillScheduleQueryRepository.getMonthSchedule(userId, startDateOfMonth, endDateOfMonth);
    }

    @Transactional
    public List<DateScheduleResponse> getDateSchedule(Long userId, LocalDate date) {
        UserServiceUtil.existsUserById(userRepository, userId);

        return pillScheduleQueryRepository.getDateSchedule(userId, date);
    }

    @Transactional
    public List<MonthScheduleResponse> getFriendMonthSchedule(Long userId, Long friendId, LocalDate date) {
        UserServiceUtil.existsUserById(userRepository, userId);
        UserServiceUtil.existsUserById(userRepository, friendId);

        if (!friendQueryRepository.isAlreadyFriend(userId, friendId)) {
            throw new ForbiddenException(ErrorCode.NOT_FRIEND);
        }

        LocalDate startDateOfMonth = DateUtil.getStartDateOfMonth(date);
        LocalDate endDateOfMonth = DateUtil.getEndDateOfMonth(date);

        return pillScheduleQueryRepository.getMonthSchedule(friendId, startDateOfMonth, endDateOfMonth);
    }

    @Transactional
    public List<DateScheduleResponse> getFriendDateSchedule(Long userId, Long friendId, LocalDate date) {
        UserServiceUtil.existsUserById(userRepository, userId);
        UserServiceUtil.existsUserById(userRepository, friendId);

        if (!friendQueryRepository.isAlreadyFriend(userId, friendId)) {
            throw new ForbiddenException(ErrorCode.NOT_FRIEND);
        }

        return pillScheduleQueryRepository.getDateSchedule(friendId, date);
    }

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
