package io.sobok.SobokSobok.notice.ui.dto;

import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.domain.NoticeType;

import java.time.LocalDateTime;

public record NoticeInfo(

        Long noticeId,
        NoticeType section,
        NoticeStatus isOkay,
        LocalDateTime createdAt,
        String senderName,
        String pillName,
        Long pillId,
        Long senderGroupId
) {
}
