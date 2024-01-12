package io.sobok.SobokSobok.friend.ui.dto;

import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record HandleFriendRequest(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    NoticeStatus isOkay
) {

}
