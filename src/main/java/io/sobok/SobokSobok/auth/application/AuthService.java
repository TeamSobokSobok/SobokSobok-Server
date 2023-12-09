package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.ui.dto.SocialLoginRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialLoginResponse;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupRequest;
import io.sobok.SobokSobok.auth.ui.dto.SocialSignupResponse;

public abstract class AuthService {

    public abstract SocialSignupResponse signup(SocialSignupRequest request);

    public abstract SocialLoginResponse login(SocialLoginRequest request);
}
