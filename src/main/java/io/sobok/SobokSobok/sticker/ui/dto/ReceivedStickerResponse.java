package io.sobok.SobokSobok.sticker.ui.dto;

public record ReceivedStickerResponse(
    Long likeScheduleId,
    Long scheduleId,
    Long stickerId,
    String stickerImg,
    String username,
    Boolean isMySticker
) {

}
