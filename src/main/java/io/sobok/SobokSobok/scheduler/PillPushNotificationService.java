package io.sobok.SobokSobok.scheduler;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.external.firebase.FCMPushService;
import io.sobok.SobokSobok.external.firebase.dto.PushNotificationRequest;
import io.sobok.SobokSobok.pill.application.PillServiceUtil;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PillPushNotificationService {

    private final FCMPushService fcmPushService;

    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final PillScheduleRepository pillScheduleRepository;

    @Scheduled(cron = "0 * * * * * ")
    public void pillTimePushNotification() {
        String currentTime = getCurrentTimeAsString("HH:mm:ss");
        LocalDate today = LocalDate.now();
        notifyPillSchedule(today, currentTime);
    }

    private void notifyPillSchedule(LocalDate date, String time) {
        List<PillSchedule> pillSchedules = pillScheduleRepository.findAllByScheduleDateAndScheduleTime(date, time);
        for (PillSchedule pillSchedule : pillSchedules) {
            Pill pill = PillServiceUtil.findPillById(pillRepository, pillSchedule.getPillId());
            User user = UserServiceUtil.findUserById(userRepository, pill.getUserId());

            fcmPushService.sendNotificationByToken(PushNotificationRequest.builder()
                    .userId(pill.getUserId())
                    .title("[" + pill.getPillName() + "]" + DateUtil.getKoreanTime(time))
                    .body("소중한 '" + user.getUsername() + "'님, 약 드실 시간이에요\n다 드시면 앱에서 체크 버튼을 눌러주세요")
                    .build()
            );
        }
    }

    private String getCurrentTimeAsString(String pattern) {
        LocalTime nowTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return nowTime.format(formatter);
    }
}
