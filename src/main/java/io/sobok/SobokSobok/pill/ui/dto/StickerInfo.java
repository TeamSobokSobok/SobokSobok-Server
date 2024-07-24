package io.sobok.SobokSobok.pill.ui.dto;

import lombok.Builder;

@Builder
public record StickerInfo(
    Long stickerId,
    Long likeScheduleId
) {

}
