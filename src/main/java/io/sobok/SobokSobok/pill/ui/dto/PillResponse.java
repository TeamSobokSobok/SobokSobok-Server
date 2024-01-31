package io.sobok.SobokSobok.pill.ui.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PillResponse(

        String pillName,
        String scheduleDay,
        LocalDate startDate,
        LocalDate endDate,
        List<String> scheduleTime
) {
}
