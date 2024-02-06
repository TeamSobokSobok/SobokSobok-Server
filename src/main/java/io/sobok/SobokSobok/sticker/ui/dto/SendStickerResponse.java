package io.sobok.SobokSobok.sticker.ui.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SendStickerResponse(
    Long id,
    Long scheduleId,
    Long senderId,
    Long stickerId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
