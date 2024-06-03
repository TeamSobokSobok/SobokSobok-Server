package io.sobok.SobokSobok.external.firebase;

import com.google.firebase.messaging.*;
import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
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

        Message message = Message.builder()
                .setToken(user.getDeviceToken())
                .setNotification(
                        Notification.builder()
                                .setTitle(request.title())
                                .setBody(request.body())
                                .build()
                )
                .setAndroidConfig(
                        AndroidConfig.builder()
                                .setNotification(
                                        AndroidNotification.builder()
                                                .setTitle(request.title())
                                                .setBody(request.body())
                                                .setClickAction("push_click")
                                                .build()
                                )
                                .build()
                )
                .setApnsConfig(
                        ApnsConfig.builder()
                                .setAps(Aps.builder()
                                        .setCategory("push_click")
                                        .build())
                                .build()
                )
                .putData("title", request.title())
                .putData("body", request.body() == null ? "" : request.body())
                .putData("type", request.type())
                .build();

        sendMessageToFirebase(message, user.getId());
    }

    public void sendNotificationByTokenWithFriendData(PushNotificationRequest request) {
        User user = UserServiceUtil.findUserById(userRepository, request.userId());

        Message message = Message.builder()
                .setToken(user.getDeviceToken())
                .setNotification(
                        Notification.builder()
                                .setTitle(request.title())
                                .setBody(request.body())
                                .build()
                )
                .setAndroidConfig(
                        AndroidConfig.builder()
                                .setNotification(
                                        AndroidNotification.builder()
                                                .setTitle(request.title())
                                                .setBody(request.body())
                                                .setClickAction("push_click")
                                                .build()
                                )
                                .build()
                )
                .setApnsConfig(
                        ApnsConfig.builder()
                                .setAps(Aps.builder()
                                        .setCategory("push_click")
                                        .build())
                                .build()
                )
                .putData("title", request.title())
                .putData("body", request.body() == null ? "" : request.body())
                .putData("type", request.type())
                .putData("friendId", request.data().get("friendId"))
                .build();

        sendMessageToFirebase(message, user.getId());
    }

    private void sendMessageToFirebase(Message message, Long userId) {
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send push notification. userId: {}\n{}", userId, e.getMessage());
        }
    }
}
