package io.sobok.SobokSobok.friend.ui;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.friend.application.FriendService;
import io.sobok.SobokSobok.friend.ui.dto.AddFriendRequest;
import io.sobok.SobokSobok.friend.ui.dto.AddFriendResponse;
import io.sobok.SobokSobok.friend.ui.dto.FriendListResponse;
import io.sobok.SobokSobok.friend.ui.dto.HandleFriendRequest;
import io.sobok.SobokSobok.friend.ui.dto.HandleFriendRequestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Tag(name = "Group", description = "공유 관련 컨트롤러")
public class FriendController {

    final FriendService friendService;

    @PostMapping("")
    @Operation(
        summary = "공유 요청 API 메서드",
        description = "캘린더 공유를 요청하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<AddFriendResponse>> addFriend(
        @AuthenticationPrincipal User user,
        @RequestBody @Valid final AddFriendRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.ADD_FRIEND_SUCCESS,
                friendService.addFriend(user.getId(), request)
            ));
    }

    @PutMapping("/{noticeId}")
    @Operation(
        summary = "공유 수락 API 메서드",
        description = "캘린더 공유를 수락 혹은 거절하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<HandleFriendRequestResponse>> handleFriendRequest(
        @AuthenticationPrincipal User user,
        @PathVariable Long noticeId,
        @RequestBody @Valid HandleFriendRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.HANDLE_FRIEND_REQUEST_SUCCESS,
                friendService.updateNoticeStatus(user.getId(), noticeId, request)
            ));
    }

    @GetMapping("")
    @Operation(
        summary = "친구 리스트 조회 API 메서드",
        description = "친구 리스트를 조회하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<List<FriendListResponse>>> getFriendList(
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.GET_FRIEND_LIST_SUCCESS,
                friendService.getFriendList(user.getId())
            ));
    }
}
