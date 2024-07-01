package io.sobok.SobokSobok.external.aws.sqs;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Component
public class SQSMessageSender {

    private final SqsTemplate queueMessagingTemplate;

    @Value("${spring.cloud.aws.sqs.name}")
    private String QUEUE_NAME;

    public SQSMessageSender(SqsAsyncClient sqsAsyncClient) {
        this.queueMessagingTemplate = SqsTemplate.newTemplate(sqsAsyncClient);
    }

    public SendResult<SQSPushNotificationRequest> sendMessage(SQSPushNotificationRequest request) {
        return queueMessagingTemplate.send(to -> to
                .queue(QUEUE_NAME)
                .payload(request));
    }
}
