package io.sobok.SobokSobok.friend.ui;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.sobok.SobokSobok.friend.application.FriendService;
import io.sobok.SobokSobok.friend.ui.dto.AddFriendResponse;
import io.sobok.SobokSobok.friend.ui.dto.FriendListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        @RequestParam final Long memberId,
        @RequestParam final String friendName
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.ADD_FRIEND_SUCCESS,
                friendService.addFriend(user.getId(), memberId, friendName)
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
