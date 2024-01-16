package io.sobok.SobokSobok.friend.ui.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateFriendName(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String friendName
) {

}
