package io.sobok.SobokSobok.auth.ui;

import io.sobok.SobokSobok.auth.application.UserService;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.ui.dto.UsernameRequest;
import io.sobok.SobokSobok.auth.ui.dto.UsernameResponse;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "유저 관련 컨트롤러")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    @Operation(
            summary = "닉네임 중복 확인 API 메서드",
            description = "사용자 닉네임의 사용여부를 반환해주는 메서드입니다. true -> 사용중, false -> 사용 가능"
    )
    public ResponseEntity<ApiResponse<Boolean>> isNicknameDuplicate(@RequestParam final String username) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.NICKNAME_CHECK_SUCCESS,
                        userService.duplicateNickname(username)
                ));
    }

    @GetMapping("/search")
    @Operation(
        summary = "유저 닉네임 조회 API 메서드",
        description = "공유 멤버(유저) 닉네임을 조회하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<List<UsernameResponse>>> getUsername(
        @AuthenticationPrincipal User user,
        @RequestParam final String username
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                SuccessCode.GET_USERNAME_SUCCESS,
                userService.getUsername(user.getId(), username)
            ));
    }

    @PutMapping("/nickname")
    @Operation(
            summary = "유저 닉네임 변경 API 메서드",
            description = "유저 본인의 닉네임을 변경하는 메서드입니다."
    )
    public ResponseEntity<ApiResponse<Void>> changeUsername(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid final UsernameRequest request
    ) {

        userService.changeUsername(user.getId(), request.username());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.CHANGE_NICKNAME_SUCCESS
                ));
    }
}
