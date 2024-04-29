package io.sobok.SobokSobok.external.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.Platform;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.external.firebase.dto.PushNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMPushService {

    private final FirebaseMessaging firebaseMessaging;

    private final UserRepository userRepository;

    public void sendNotificationByToken(PushNotificationRequest request) {
        User user = UserServiceUtil.findUserById(userRepository, request.userId());
        if (user.getPlatform() == Platform.iOS) {
            sendNotification(user, request);
        } else {
            sendDataMessage(user, request);
        }
    }

    private void sendNotification(User user, PushNotificationRequest request) {
        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();

        Message message = Message.builder()
                .setToken(user.getDeviceToken())
                .setNotification(notification)
                .build();

        sendMessageToFirebase(message, request.userId());
    }

    private void sendDataMessage(User user, PushNotificationRequest request) {
        Message message = Message.builder()
                .putData("title", request.title())
                .putData("body", request.body())
                .setToken(user.getDeviceToken())
                .build();

        sendMessageToFirebase(message, request.userId());
    }

    private void sendMessageToFirebase(Message message, Long userId) {
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send push notification. userId: {}\n{}", userId, e.getMessage());
        }
    }
}
