package io.sobok.SobokSobok.friend.ui.dto;

import lombok.Builder;

@Builder
public record FriendListResponse(
    Long friendId,
    Long memberId,
    String friendName
) {

}
