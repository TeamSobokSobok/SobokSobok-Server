package io.sobok.SobokSobok.notice.ui.dto;

import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CompletePillNoticeRequest(

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        NoticeStatus isOkay
) {
}
