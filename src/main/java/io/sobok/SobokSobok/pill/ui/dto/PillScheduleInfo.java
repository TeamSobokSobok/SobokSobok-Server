package io.sobok.SobokSobok.pill.ui.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PillScheduleInfo(

        Long scheduleId,
        Long pillId,
        String pillName,
        Boolean isCheck,
        Integer color,
        List<Long> stickerId,
        Long stickerTotalCount
) {

}
