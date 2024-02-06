package io.sobok.SobokSobok.external.firebase.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record FCMNotificationRequest(

        Long userId,
        String title,
        String body,
        String image,
        Map<String, String> data
) {
}
