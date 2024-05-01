package io.sobok.SobokSobok.pill.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.external.firebase.FCMPushService;
import io.sobok.SobokSobok.external.firebase.dto.PushNotificationRequest;
import io.sobok.SobokSobok.friend.infrastructure.FriendQueryRepository;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.domain.NoticeType;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.domain.SendPill;
import io.sobok.SobokSobok.pill.infrastructure.*;
import io.sobok.SobokSobok.pill.ui.dto.PillListResponse;
import io.sobok.SobokSobok.pill.ui.dto.PillRequest;
import io.sobok.SobokSobok.pill.ui.dto.PillResponse;
import io.sobok.SobokSobok.utils.PillUtil;
import lombok.RequiredArgsConstructor;
import io.sobok.SobokSobok.pill.infrastructure.PillQueryRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PillService {

    private final FCMPushService fcmPushService;

    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final PillScheduleRepository pillScheduleRepository;
    private final SendPillRepository sendPillRepository;
    private final NoticeRepository noticeRepository;

    private final FriendQueryRepository friendQueryRepository;
    private final PillQueryRepository pillQueryRepository;
    private final PillScheduleQueryRepository pillScheduleQueryRepository;

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

        if (!friendQueryRepository.isAlreadyFriend(userId, friendId)) {
            throw new ForbiddenException(ErrorCode.NOT_FRIEND);
        }

        validatePillCount(friendId, request.pillName().length);
        validatePillRequest(request.startDate(), request.endDate(), request.day(), request.timeList());

        Notice newNotice = noticeRepository.save(Notice.newInstance(userId, friendId, NoticeType.PILL, NoticeStatus.WAITING));
        StringBuilder pillNameSentence = new StringBuilder();

        for (int index = 0; index < request.pillName().length; index++) {
            String pill = request.pillName()[index];
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

            if (index == (request.pillName().length - 1)) {
                pillNameSentence.append(pill);
                break;
            }

            pillNameSentence.append(pill).append(" / ");
        }

        String receiverUsername = friendQueryRepository.getFriendName(userId, friendId);

        fcmPushService.sendNotificationByToken(PushNotificationRequest.builder()
                .userId(friendId)
                .title(receiverUsername + "님이 보낸 약 일정을 확인해보세요!")
                .body(pillNameSentence.toString())
                        .type("notice")
                .build());
    }


    @Transactional
    public Integer getPillCount(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(ErrorCode.UNREGISTERED_USER);
        }

        return pillQueryRepository.getPillCount(userId);
    }

    @Transactional
    public void deletePill(Long userId, Long pillId) {

        UserServiceUtil.existsUserById(userRepository, userId);
        Pill pill = PillServiceUtil.findPillById(pillRepository, pillId);

        if (!pill.isPillUser(userId)) {
            throw new ForbiddenException(ErrorCode.UNAUTHORIZED_PILL);
        }

        pillRepository.delete(pill);
        pillScheduleRepository.deleteAllByPillId(pillId);
    }

    @Transactional
    public List<PillListResponse> getPillList(Long userId) {

        UserServiceUtil.existsUserById(userRepository, userId);

        List<Pill> pillList = pillRepository.findAllByUserId(userId);
        return pillList.stream()
                .map(pill -> PillListResponse.builder()
                        .id(pill.getId())
                        .color(pill.getColor())
                        .pillName(pill.getPillName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PillResponse getPillInfo(Long userId, Long pillId) {

        UserServiceUtil.existsUserById(userRepository, userId);
        Pill pill = PillServiceUtil.findPillById(pillRepository, pillId);

        if (!pill.isPillUser(userId)) {
            throw new ForbiddenException(ErrorCode.UNAUTHORIZED_PILL);
        }

        List<String> scheduleTime = pillScheduleQueryRepository.getPillScheduleTime(pill.getId());

        return PillResponse.builder()
                .pillName(pill.getPillName())
                .scheduleDay(pill.getScheduleDay())
                .startDate(pill.getStartDate())
                .endDate(pill.getEndDate())
                .scheduleTime(scheduleTime)
                .build();
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
