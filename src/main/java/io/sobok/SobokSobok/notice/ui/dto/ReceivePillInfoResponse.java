package io.sobok.SobokSobok.notice.ui.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReceivePillInfoResponse(

        String pillName,
        List<String> scheduleTime,
        LocalDate startDate,
        LocalDate endDate,
        String scheduleDay
) {
}
