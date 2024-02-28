package io.sobok.SobokSobok.external.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.external.firebase.dto.PushNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMPushService {

    private final FirebaseMessaging firebaseMessaging;

    private final UserRepository userRepository;

    public void sendNotificationByToken(PushNotificationRequest request) {

        User user = UserServiceUtil.findUserById(userRepository, request.userId());

        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();

        Message message = Message.builder()
                .setToken(user.getDeviceToken())
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new IllegalArgumentException("푸시알림 전송에 실패했습니다. userId: " + request.userId() + "\n" + e.getMessage());
        }
    }
}
