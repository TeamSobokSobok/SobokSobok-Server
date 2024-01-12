package io.sobok.SobokSobok.friend.ui.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddFriendRequest(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long memberId,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String friendName
) {

}
