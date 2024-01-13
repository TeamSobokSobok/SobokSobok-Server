package io.sobok.SobokSobok.notice.ui.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NoticeResponse(

        String username,
        List<NoticeInfo> infoList
) {
}
