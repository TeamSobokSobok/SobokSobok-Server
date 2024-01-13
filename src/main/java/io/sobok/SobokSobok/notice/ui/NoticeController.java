package io.sobok.SobokSobok.notice.ui;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.notice.application.NoticeService;
import io.sobok.SobokSobok.notice.ui.dto.NoticeResponse;
import io.sobok.SobokSobok.notice.ui.dto.ReceivePillInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Tag(name = "Notice", description = "알림 관련 컨트롤러")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("")
    @Operation(
            summary = "알림 전체 조회 API 메서드",
            description = "모든 알림을 조회하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<NoticeResponse>> getList(@AuthenticationPrincipal User user) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.GET_NOTICE_LIST_SUCCESS,
                        noticeService.getList(user.getId())
                ));
    }

    @GetMapping("/list/{noticeId}/{pillId}")
    @Operation(
            summary = "전달받은 약 정보 조회 API 메서드",
            description = "친구에게 전달받은 약의 상세정보를 조회하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<ReceivePillInfoResponse>> getReceivePillInfo(
            @AuthenticationPrincipal User user,
            @PathVariable Long noticeId,
            @PathVariable Long pillId
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.GET_RECEIVE_PILL_INFO_SUCCESS,
                        noticeService.getReceivePillInfo(user.getId(), noticeId, pillId)
                ));
    }
}
