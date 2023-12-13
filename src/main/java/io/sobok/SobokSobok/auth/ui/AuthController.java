package io.sobok.SobokSobok.auth.ui;

import io.sobok.SobokSobok.auth.application.AuthService;
import io.sobok.SobokSobok.auth.application.AuthServiceProvider;
import io.sobok.SobokSobok.auth.domain.SocialType;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginResponse;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupResponse;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증 관련 컨트롤러")
public class AuthController {

    private final AuthServiceProvider authServiceProvider;

    @PostMapping("/signup")
    @Operation(
            summary = "소셜 회원가입 API 메서드",
            description = "사용자의 소셜 정보로 소복소복에 회원가입하는 API 입니다."
    )
    public ResponseEntity<ApiResponse<SocialSignupResponse>> signup(@RequestBody @Valid final SocialSignupRequest request) {

        AuthService authService = authServiceProvider.getAuthService(request.socialType());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        SuccessCode.SOCIAL_SIGNUP_SUCCESS,
                        authService.signup(request)
                ));
    }

    @GetMapping("/login")
    @Operation(
            summary = "소셜 로그인 API 메서드",
            description = "사용자의 소셜 정보로 소복소복에 로그인하는 API 입니다."
    )
    public ResponseEntity<ApiResponse<SocialLoginResponse>> login(
            @RequestParam final String code,
            @RequestParam final SocialType socialType,
            @RequestParam final String deviceToken
    ) {

        AuthService authService = authServiceProvider.getAuthService(socialType);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.SOCIAL_LOGIN_SUCCESS,
                        authService.login(SocialLoginRequest.builder()
                                .code(code)
                                .socialType(socialType)
                                .deviceToken(deviceToken)
                                .build())
                ));
    }
}
