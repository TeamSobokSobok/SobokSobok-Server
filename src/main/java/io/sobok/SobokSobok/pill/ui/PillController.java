package io.sobok.SobokSobok.pill.ui;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.pill.application.PillService;
import io.sobok.SobokSobok.pill.ui.dto.PillRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pill")
@Tag(name = "Pill", description = "약 관련 컨트롤러")
public class PillController {

    private final PillService pillService;

    @PostMapping("")
    @Operation(
            summary = "약 추가 API 메서드",
            description = "내 약을 추가하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<Void>> addPill(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid final PillRequest request
    ) {

        pillService.addPill(user.getId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.ADD_PILL_SUCCESS));
    }
}