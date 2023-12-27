package io.sobok.SobokSobok.pill.ui.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PillRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String[] pillName,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String day,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String[] timeList,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate startDate,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate endDate
) { }
