package io.sobok.SobokSobok.pill.ui;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.pill.application.PillService;
import io.sobok.SobokSobok.pill.ui.dto.PillListResponse;
import io.sobok.SobokSobok.pill.ui.dto.PillRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/count")
    @Operation(
            summary = "내 약 개수 조회 API 메서드",
            description = "내 약의 개수가 몇 개인지 확인하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<Integer>> getPillCount(@AuthenticationPrincipal User user) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.GET_PILL_COUNT_SUCCESS,
                        pillService.getPillCount(user.getId())
                ));
    }

    @GetMapping("/count/{userId}")
    @Operation(
            summary = "친구 약 개수 조회 API 메서드",
            description = "친구 약의 개수가 몇 개인지 확인하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<Integer>> getUserPillCount(@PathVariable Long userId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.GET_PILL_COUNT_SUCCESS,
                        pillService.getPillCount(userId)
                ));
    }

    @PostMapping("/friend/{friendId}")
    @Operation(
            summary = "친구에게 약 전송 API 메서드",
            description = "친구에게 약을 전송하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<Void>> sendPillToFriend(
            @AuthenticationPrincipal User user,
            @PathVariable Long friendId,
            @RequestBody @Valid final PillRequest request
    ) {

        pillService.sendPill(user.getId(), friendId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.SEND_PILL_SUCCESS));
    }

    @DeleteMapping("/{pillId}")
    @Operation(
            summary = "약 삭제 API 메서드",
            description = "내 약을 삭제하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<Void>> deletePill(@AuthenticationPrincipal User user, @PathVariable Long pillId) {

        pillService.deletePill(user.getId(), pillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.DELETE_PILL_SUCCESS));
    }

    @GetMapping("/list")
    @Operation(
            summary = "약 리스트 조회 API 메서드",
            description = "내 약의 리스트를 조회하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<List<PillListResponse>>> getPillList(@AuthenticationPrincipal User user) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessCode.GET_PILL_LIST_SUCCESS,
                                pillService.getPillList(user.getId())
                        )
                );
    }
}
