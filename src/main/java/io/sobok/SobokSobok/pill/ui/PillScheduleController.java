package io.sobok.SobokSobok.pill.ui;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.pill.application.PillScheduleService;
import io.sobok.SobokSobok.pill.ui.dto.CheckPillScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Tag(name = "Schedule", description = "약 일정 관련 컨트롤러")
public class PillScheduleController {

    private final PillScheduleService pillScheduleService;

    @PutMapping("/check/{scheduleId}")
    @Operation(
        summary = "복용 체크 완료 API 메서드",
        description = "약 일정의 복용 체크를 완료하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<CheckPillScheduleResponse>> checkPillSchedule(
        @AuthenticationPrincipal User user,
        @PathVariable Long scheduleId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.CHECK_PILL_SCHEDULE_SUCCESS,
                pillScheduleService.changePillScheduleCheck(user.getId(), scheduleId, true)
            ));
    }

    @PutMapping("/uncheck/{scheduleId}")
    @Operation(
        summary = "복용 체크 취소 API 메서드",
        description = "약 일정의 복용 체크를 취소하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<CheckPillScheduleResponse>> uncheckPillSchedule(
        @AuthenticationPrincipal User user,
        @PathVariable Long scheduleId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.UNCHECK_PILL_SCHEDULE_SUCCESS,
                pillScheduleService.changePillScheduleCheck(user.getId(), scheduleId, false)
            ));
    }

}
