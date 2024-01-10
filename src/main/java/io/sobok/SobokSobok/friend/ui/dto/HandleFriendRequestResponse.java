package io.sobok.SobokSobok.friend.ui.dto;

import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record HandleFriendRequestResponse(
    Long noticeId,
    String memberName,
    NoticeStatus isOkay,
    LocalDateTime updatedAt
) {

}
