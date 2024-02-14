package io.sobok.SobokSobok.auth.ui.dto;

import io.sobok.SobokSobok.auth.domain.SocialType;
import lombok.Builder;

@Builder
public record UsernameResponse(
    Long memberId,
    String memberName,
    SocialType deviceOS,
    Boolean selfCheck
) {

}
