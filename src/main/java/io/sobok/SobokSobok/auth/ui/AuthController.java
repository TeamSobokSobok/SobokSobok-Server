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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceProvider authServiceProvider;

    @PostMapping("/signup")
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
