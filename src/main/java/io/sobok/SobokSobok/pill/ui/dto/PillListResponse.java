package io.sobok.SobokSobok.pill.ui.dto;

import lombok.Builder;

@Builder
public record PillListResponse(

        Long id,
        Integer color,
        String pillName
) {
}
