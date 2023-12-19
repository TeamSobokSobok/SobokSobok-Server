package io.sobok.SobokSobok.auth.application;

import io.sobok.SobokSobok.auth.ui.dto.*;

public abstract class SocialService {

    public abstract SocialSignupResponse signup(SocialSignupRequest request);

    public abstract SocialLoginResponse login(SocialLoginRequest request);
}
