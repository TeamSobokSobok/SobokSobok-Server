package io.sobok.SobokSobok.sticker.ui;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.sticker.application.StickerService;
import io.sobok.SobokSobok.sticker.ui.dto.ReceivedStickerResponse;
import io.sobok.SobokSobok.sticker.ui.dto.StickerActionResponse;
import io.sobok.SobokSobok.sticker.ui.dto.StickerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/{scheduleId}")
    @Operation(
        summary = "스티커 전송 API 메서드",
        description = "스티커를 전송하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<StickerActionResponse>> sendSticker(
        @AuthenticationPrincipal User user,
        @PathVariable Long scheduleId,
        @RequestParam Long stickerId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.SEND_STICKER_SUCCESS,
                stickerService.sendSticker(user.getId(), scheduleId, stickerId)
            ));
    }

    @PutMapping("/my/{likeScheduleId}")
    @Operation(
        summary = "보낸 스티커 수정 API 메서드",
        description = "보낸 스티커를 수정하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<StickerActionResponse>> updateSendSticker(
        @AuthenticationPrincipal User user,
        @PathVariable Long likeScheduleId,
        @RequestParam Long stickerId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.UPDATE_STICKER_SUCCESS,
                stickerService.updateSendSticker(user.getId(), likeScheduleId, stickerId)
            ));
    }

    @GetMapping("/{scheduleId}")
    @Operation(
        summary = "받은 스티커 전체 조회 API 메서드",
        description = "받은 스티커를 전체 조회하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<List<ReceivedStickerResponse>>> getReceivedStickerList(
        @AuthenticationPrincipal User user,
        @PathVariable Long scheduleId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.GET_STICKER_LIST_SUCCESS,
                stickerService.getReceivedStickerList(user.getId(), scheduleId)
            ));
    }
}
