package io.sobok.SobokSobok.auth.ui;

import io.sobok.SobokSobok.auth.application.AuthService;
import io.sobok.SobokSobok.auth.application.SocialService;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.ui.dto.*;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증 관련 컨트롤러")
public class AuthController {

    private final AuthService authService;
    private final SocialService socialService;

    @PostMapping("/signup")
    @Operation(
            summary = "소셜 회원가입 API 메서드",
            description = "사용자의 소셜 정보로 소복소복에 회원가입하는 API 입니다."
    )
    public ResponseEntity<ApiResponse<SocialSignupResponse>> signup(@RequestBody @Valid final SocialSignupRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        SuccessCode.SOCIAL_SIGNUP_SUCCESS,
                        socialService.signup(request)
                ));
    }

    @GetMapping("/login")
    @Operation(
            summary = "소셜 로그인 API 메서드",
            description = "사용자의 소셜 정보로 소복소복에 로그인하는 API 입니다."
    )
    public ResponseEntity<ApiResponse<SocialLoginResponse>> login(
            @RequestParam final String socialId,
            @RequestParam final String deviceToken
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.SOCIAL_LOGIN_SUCCESS,
                        socialService.login(SocialLoginRequest.builder()
                                .socialId(socialId)
                                .deviceToken(deviceToken)
                                .build())
                ));
    }

    @GetMapping("/logout")
    @Operation(
            summary = "로그아웃 API 메서드",
            description = "사용자의 로그아웃 기능을 하는 API 입니다."
    )
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal User user) {

        authService.logout(user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.LOGOUT_SUCCESS
                ));
    }

    @GetMapping("/refresh")
    @Operation(
            summary = "JWT 토큰 재발급",
            description = "Refresh token을 통해 JWT 토큰을 재발급받는 API 입니다."
    )
    public ResponseEntity<ApiResponse<JwtTokenResponse>> refresh(
            @RequestHeader("Refresh-Token") final String refreshToken
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.TOKEN_REFRESH_SUCCESS,
                        authService.refresh(refreshToken)
                ));
    }

    @DeleteMapping("/user/leave")
    @Operation(
            summary = "소복소복 회원 탈퇴",
            description = "소복소복 서비스를 탈퇴하는 API 입니다."
    )
    public ResponseEntity<ApiResponse<Void>> leave(
            @RequestBody @Valid final LeaveRequest request,
            @AuthenticationPrincipal User user
    ) {

        authService.leave(user.getId(), request.text());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.USER_LEAVE_SUCCESS
                ));
    }
}
