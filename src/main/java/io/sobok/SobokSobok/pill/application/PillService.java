package io.sobok.SobokSobok.pill.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.friend.infrastructure.FriendQueryRepository;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.domain.NoticeType;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.domain.SendPill;
import io.sobok.SobokSobok.pill.infrastructure.PillQueryRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.pill.infrastructure.SendPillRepository;
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
    private final PillScheduleRepository pillScheduleRepository;
    private final SendPillRepository sendPillRepository;
    private final NoticeRepository noticeRepository;

    private final FriendQueryRepository friendQueryRepository;
    private final PillQueryRepository pillQueryRepository;

    @Transactional
    public void addPill(Long userId, PillRequest request) {

        User user = UserServiceUtil.findUserById(userRepository, userId);

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
    public void sendPill(Long userId, Long friendId, PillRequest request) {

        UserServiceUtil.existsUserById(userRepository, userId);
        UserServiceUtil.existsUserById(userRepository, friendId);

        if (!friendQueryRepository.isAlreadyFriend(userId, friendId)) {
            throw new ForbiddenException(ErrorCode.NOT_FRIEND);
        }

        validatePillCount(friendId, request.pillName().length);
        validatePillRequest(request.startDate(), request.endDate(), request.day(), request.timeList());

        Notice newNotice = noticeRepository.save(Notice.newInstance(userId, friendId, NoticeType.PILL, NoticeStatus.WAITING));

        for (String pill : request.pillName()) {
            Pill newPill = pillRepository.save(Pill.builder()
                    .pillName(pill)
                    .color(PillUtil.getRandomColorNumber())
                    .startDate(request.startDate())
                    .endDate(request.endDate())
                    .scheduleDay(request.day())
                    .userId(null)
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

            sendPillRepository.save(SendPill.newInstance(newNotice.getId(), newPill.getId()));
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
