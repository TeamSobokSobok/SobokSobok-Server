package io.sobok.SobokSobok.friend.ui.dto;

import lombok.Builder;

@Builder
public record UpdateFriendNameResponse(
    Long friendId,
    Long userId,
    Long memberId,
    String friendName
) {

}
