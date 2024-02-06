package io.sobok.SobokSobok.sticker.application;

import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.sticker.infrastructure.StickerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StickerServiceUtil {

    public static void existsStickerById(StickerRepository stickerRepository, Long id) {
        if (!stickerRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.UNREGISTERED_STICKER);
        }
    }
}
