package io.sobok.SobokSobok.sticker.application;

import io.sobok.SobokSobok.sticker.infrastructure.StickerRepository;
import io.sobok.SobokSobok.sticker.ui.dto.StickerResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StickerService {

    private final StickerRepository stickerRepository;

    @Transactional
    public List<StickerResponse> getStickerList() {
        return stickerRepository.findAll().stream().map(
            sticker -> StickerResponse.builder()
                .stickerId(sticker.getId())
                .stickerImg(sticker.getStickerImg())
                .build()
        ).collect(Collectors.toList());
    }
}
