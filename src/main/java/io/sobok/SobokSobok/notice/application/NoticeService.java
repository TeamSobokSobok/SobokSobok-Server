package io.sobok.SobokSobok.notice.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.infrastructure.NoticeQueryRepository;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;
import io.sobok.SobokSobok.notice.ui.dto.NoticeInfo;
import io.sobok.SobokSobok.notice.ui.dto.NoticeResponse;
import io.sobok.SobokSobok.notice.ui.dto.ReceivePillInfoResponse;
import io.sobok.SobokSobok.pill.application.PillServiceUtil;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeQueryRepository noticeQueryRepository;

    @Transactional
    public NoticeResponse getList(Long userId) {

        User user = UserServiceUtil.findUserById(userRepository, userId);

        List<NoticeInfo> noticeList = noticeQueryRepository.getNoticeList(userId);

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
}
