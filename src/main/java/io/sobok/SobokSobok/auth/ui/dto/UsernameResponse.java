package io.sobok.SobokSobok.auth.ui.dto;

import lombok.Builder;

@Builder
public record UsernameResponse(
    Long memberId,
    String memberName,
    Boolean selfCheck
) {

}
