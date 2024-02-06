package io.sobok.SobokSobok.sticker.ui;

import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.sticker.application.StickerService;
import io.sobok.SobokSobok.sticker.ui.dto.StickerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sticker")
@Tag(name = "Sticker", description = "스티커 관련 컨트롤러")
public class StickerController {

    private final StickerService stickerService;

    @GetMapping("")
    @Operation(
        summary = "전송할 스티커 전체보기 API 메서드",
        description = "전송할 스티커 전체를 조회하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<List<StickerResponse>>> getStickerList() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.GET_STICKER_LIST_SUCCESS,
                stickerService.getStickerList()
            ));
    }
}
