package io.sobok.SobokSobok.notice.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.external.firebase.FCMPushService;
import io.sobok.SobokSobok.external.firebase.dto.PushNotificationRequest;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.infrastructure.NoticeQueryRepository;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;
import io.sobok.SobokSobok.notice.ui.dto.NoticeInfo;
import io.sobok.SobokSobok.notice.ui.dto.NoticeResponse;
import io.sobok.SobokSobok.notice.ui.dto.ReceivePillInfoResponse;
import io.sobok.SobokSobok.pill.application.PillServiceUtil;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.SendPill;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.pill.infrastructure.SendPillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final FCMPushService fcmPushService;

    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final PillScheduleRepository pillScheduleRepository;
    private final SendPillRepository sendPillRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeQueryRepository noticeQueryRepository;

    @Transactional
    public NoticeResponse getList(Long userId) {

        User user = UserServiceUtil.findUserById(userRepository, userId);

        List<NoticeInfo> noticeList = noticeQueryRepository.getNoticeList(userId);
        Comparator<NoticeInfo> compareByCreatedAt = Comparator.comparing(NoticeInfo::createdAt).reversed();
        noticeList.sort(compareByCreatedAt);

        return NoticeResponse.builder()
                .username(user.getUsername())
                .infoList(noticeList)
                .build();
    }

    @Transactional
    public ReceivePillInfoResponse getReceivePillInfo(Long userId, Long noticeId, Long pillId) {

        User receiver = UserServiceUtil.findUserById(userRepository, userId);
        PillServiceUtil.existsPillById(pillRepository, pillId);
        Notice notice = NoticeServiceUtil.findNoticeById(noticeRepository, noticeId);

        if (!notice.isPillNotice()) {
            throw new BadRequestException(ErrorCode.NOT_PILL_NOTICE);
        }

        if (!notice.getReceiverId().equals(receiver.getId())) {
            throw new ForbiddenException(ErrorCode.UNAUTHORIZED_PILL);
        }

        if (notice.isCompleteNotice()) {
            throw new BadRequestException(ErrorCode.ALREADY_COMPLETE_NOTICE);
        }

        return noticeQueryRepository.getReceivePillInfo(noticeId, pillId);
    }

    @Transactional
    public void completePillNotice(Long userId, Long pillId, NoticeStatus isOkay) {

        User receiver = UserServiceUtil.findUserById(userRepository, userId);
        Pill pill = PillServiceUtil.findPillById(pillRepository, pillId);

        SendPill sendPill = sendPillRepository.findByPillId(pillId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_SEND_PILL));
        Notice notice = noticeRepository.findById(sendPill.getNoticeId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NON_EXISTS_NOTICE));

        User sender = UserServiceUtil.findUserById(userRepository, notice.getSenderId());

        if (!notice.getReceiverId().equals(receiver.getId())) {
            throw new ForbiddenException(ErrorCode.UNAUTHORIZED_PILL);
        }

        if (notice.isCompleteNotice()) {
            throw new BadRequestException(ErrorCode.ALREADY_COMPLETE_NOTICE);
        }

        if (isOkay.equals(NoticeStatus.REFUSE)) {
            pillRepository.deleteById(pillId);
            pillScheduleRepository.deleteAllByPillId(pillId);

            fcmPushService.sendNotificationByToken(PushNotificationRequest.builder()
                    .userId(sender.getId())
                    .title(receiver.getUsername() + "님이 약 일정을 거절했어요")
                            .type("notice")
                    .build());
        }

        if (isOkay.equals(NoticeStatus.ACCEPT)) {
            pill.receivePill(receiver.getId());

            fcmPushService.sendNotificationByToken(PushNotificationRequest.builder()
                    .userId(sender.getId())
                    .title(receiver.getUsername() + "님이 약 일정을 수락했어요")
                            .type("notice")
                    .build());
        }

        notice.changeNoticeStatus(isOkay);
    }
}
