package io.sobok.SobokSobok.pill.ui.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CheckPillScheduleResponse(
    Long scheduleId,
    Long pillId,
    Long userId,
    LocalDateTime scheduleDate,
    String scheduleTime,
    Boolean isCheck
) {

}
