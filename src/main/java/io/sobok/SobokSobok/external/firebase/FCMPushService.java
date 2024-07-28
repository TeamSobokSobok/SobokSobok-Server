package io.sobok.SobokSobok.external.firebase;

import com.google.firebase.messaging.*;
import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.Platform;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
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
        sendNotification(request, null);
    }

    public void sendNotificationByTokenWithFriendData(PushNotificationRequest request) {
        sendNotification(request, request.data().get("friendId"));
    }

    private void sendNotification(PushNotificationRequest request, String friendId) {
        User user = UserServiceUtil.findUserById(userRepository, request.userId());
        Message.Builder messageBuilder;
        if (user.getSocialInfo().getPlatform().equals(Platform.ANDROID)) {
            messageBuilder = Message.builder()
                    .setToken(user.getDeviceToken())
                    .putData("title", request.title())
                    .putData("body", request.body() == null ? "" : request.body())
                    .putData("type", request.type());
        } else if (user.getSocialInfo().getPlatform().equals(Platform.iOS)) {
            messageBuilder = Message.builder()
                    .setToken(user.getDeviceToken())
                    .setNotification(buildNotification(request))
                    .putData("type", request.type());
        } else {
            throw new BadRequestException(ErrorCode.INVALID_PLATFORM);
        }

        if (friendId != null) {
            messageBuilder.putData("friendId", friendId);
        }

        sendMessageToFirebase(messageBuilder.build(), user.getId());
    }

    private Notification buildNotification(PushNotificationRequest request) {
        return Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();
    }

    private void sendMessageToFirebase(Message message, Long userId) {
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send push notification. userId: {}\n{}", userId, e.getMessage());
        }
    }
}
