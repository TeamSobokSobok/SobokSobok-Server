package io.sobok.SobokSobok.friend.ui.dto;

import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import lombok.Builder;

@Builder
public record AddFriendResponse(
    Long noticeId,
    String senderName,
    String memberName,
    NoticeStatus isOkay
) {

}
