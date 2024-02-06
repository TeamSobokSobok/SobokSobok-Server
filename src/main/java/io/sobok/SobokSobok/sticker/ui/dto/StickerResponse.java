package io.sobok.SobokSobok.sticker.ui.dto;

import lombok.Builder;

@Builder
public record StickerResponse(
    Long stickerId,
    String stickerImg
) {

}
