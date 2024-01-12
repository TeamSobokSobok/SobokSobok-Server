package io.sobok.SobokSobok.notice.service;

import com.querydsl.core.Tuple;
import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.notice.infrastructure.NoticeQueryRepository;
import io.sobok.SobokSobok.notice.ui.dto.NoticeInfo;
import io.sobok.SobokSobok.notice.ui.dto.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final UserRepository userRepository;
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
}
