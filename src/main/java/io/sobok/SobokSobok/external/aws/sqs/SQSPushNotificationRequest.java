package io.sobok.SobokSobok.external.aws.sqs;

public record SQSPushNotificationRequest(

        String deviceToken,

        String title,

        String content
) {
}
