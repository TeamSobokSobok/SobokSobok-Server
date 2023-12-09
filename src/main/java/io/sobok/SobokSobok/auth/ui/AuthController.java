package io.sobok.SobokSobok.auth.ui;

import io.sobok.SobokSobok.auth.application.AuthService;
import io.sobok.SobokSobok.auth.application.AuthServiceProvider;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginResponse;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceProvider authServiceProvider;

    @PostMapping("/signup")
    public SocialSignupResponse signup(@RequestBody @Valid final SocialSignupRequest request) {
        AuthService authService = authServiceProvider.getAuthService(request.socialType());
        return authService.signup(request);
    }

    @GetMapping("/login")
    public SocialLoginResponse login(@RequestBody @Valid final SocialLoginRequest request) {
        AuthService authService = authServiceProvider.getAuthService(request.socialType());
        return authService.login(request);
    }
}
