package io.sobok.SobokSobok.pill.ui.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MonthScheduleResponse(

        LocalDate scheduleDate,
        Long scheduleCount,
        Long isCheckCount,
        String isComplete
) {
}
