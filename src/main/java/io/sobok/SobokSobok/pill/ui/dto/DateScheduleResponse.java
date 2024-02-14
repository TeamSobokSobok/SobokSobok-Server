package io.sobok.SobokSobok.pill.ui.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DateScheduleResponse(

        String scheduleTime,
        List<PillScheduleInfo> scheduleList
) {

}
