package io.sobok.SobokSobok.notice.application;

import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeServiceUtil {

    public static Notice findNoticeById(NoticeRepository noticeRepository, Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NON_EXISTS_NOTICE));
    }
}
