package io.sobok.SobokSobok.pill.ui.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record PillScheduleInfo(

    Long scheduleId,
    Long pillId,
    String pillName,
    Boolean isCheck,
    Integer color,
    List<StickerInfo> stickerId,
    Long stickerTotalCount
) {

}
