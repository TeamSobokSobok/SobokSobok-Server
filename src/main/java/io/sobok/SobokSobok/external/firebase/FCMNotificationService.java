package io.sobok.SobokSobok.external.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.external.firebase.dto.FCMNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendNotificationByDeviceToken(FCMNotificationRequest request) {

        User user = UserServiceUtil.findUserById(userRepository, request.userId());

        if (user.getDeviceToken() == null) {
            throw new NotFoundException(ErrorCode.EMPTY_DEVICE_TOKEN);
        }

        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .setImage(request.image())
                .build();

        Message message = Message.builder()
                .setToken(user.getDeviceToken())
                .setNotification(notification)
                .putAllData(request.data())
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("푸시알림 전송에 실패했습니다. userId: " + user.getId() + "\n" + e.getMessage());;
        }
    }
}
