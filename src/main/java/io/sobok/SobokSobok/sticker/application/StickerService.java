package io.sobok.SobokSobok.sticker.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.external.aws.s3.S3Service;
import io.sobok.SobokSobok.external.firebase.FCMPushService;
import io.sobok.SobokSobok.external.firebase.dto.PushNotificationRequest;
import io.sobok.SobokSobok.friend.infrastructure.FriendQueryRepository;
import io.sobok.SobokSobok.pill.application.PillScheduleServiceUtil;
import io.sobok.SobokSobok.pill.application.PillServiceUtil;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.sticker.domain.LikeSchedule;
import io.sobok.SobokSobok.sticker.domain.Sticker;
import io.sobok.SobokSobok.sticker.infrastructure.LikeScheduleQueryRepository;
import io.sobok.SobokSobok.sticker.infrastructure.LikeScheduleRepository;
import io.sobok.SobokSobok.sticker.infrastructure.StickerRepository;
import io.sobok.SobokSobok.sticker.ui.dto.ReceivedStickerResponse;
import io.sobok.SobokSobok.sticker.ui.dto.StickerActionResponse;
import io.sobok.SobokSobok.sticker.ui.dto.StickerResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StickerService {

    private final FCMPushService fcmPushService;
    private final S3Service s3Service;

    private final StickerRepository stickerRepository;
    private final UserRepository userRepository;
    private final PillScheduleRepository pillScheduleRepository;
    private final PillRepository pillRepository;
    private final FriendQueryRepository friendQueryRepository;
    private final LikeScheduleRepository likeScheduleRepository;
    private final LikeScheduleQueryRepository likeScheduleQueryRepository;

    @Transactional
    public List<StickerResponse> getStickerList() {
        return stickerRepository.findAll().stream().map(
            sticker -> StickerResponse.builder()
                .stickerId(sticker.getId())
                .stickerImg(sticker.getStickerImg())
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public StickerActionResponse sendSticker(Long userId, Long scheduleId, Long stickerId) {
        UserServiceUtil.existsUserById(userRepository, userId);
        PillSchedule pillSchedule = PillScheduleServiceUtil.findPillScheduleById(
            pillScheduleRepository, scheduleId);
        StickerServiceUtil.existsStickerById(stickerRepository, stickerId);

        if (!pillSchedule.getIsCheck()) {
            throw new BadRequestException(ErrorCode.UNCONSUMED_PILL);
        }

        Pill pill = PillServiceUtil.findPillById(pillRepository, pillSchedule.getPillId());
        User receiver = UserServiceUtil.findUserById(userRepository, pill.getUserId());
        String senderUsername = friendQueryRepository.getFriendName(receiver.getId(), userId);
        if (!friendQueryRepository.isAlreadyFriend(userId, receiver.getId())) {
            throw new ForbiddenException(ErrorCode.NOT_FRIEND);
        }

        if (likeScheduleRepository.existsBySenderIdAndScheduleId(userId, scheduleId)) {
            throw new ConflictException(ErrorCode.ALREADY_SEND_STICKER);
        }

        LikeSchedule likeSchedule = likeScheduleRepository.save(
            LikeSchedule.builder()
                .scheduleId(scheduleId)
                .senderId(userId)
                .stickerId(stickerId)
                .build()
        );

        fcmPushService.sendNotificationByToken(PushNotificationRequest.builder()
            .userId(receiver.getId())
            .title(senderUsername + "님이 " + pill.getPillName() + " 복약에 반응을 남겼어요!")
            .body("받은 스티커를 확인해보세요")
            .type("main")
            .build());

        return StickerActionResponse.builder()
            .likeScheduleId(likeSchedule.getId())
            .scheduleId(likeSchedule.getScheduleId())
            .senderId(likeSchedule.getSenderId())
            .stickerId(likeSchedule.getStickerId())
            .createdAt(likeSchedule.getCreatedAt())
            .updatedAt(likeSchedule.getUpdatedAt())
            .build();
    }

    @Transactional
    public StickerActionResponse updateSendSticker(Long userId, Long likeScheduleId,
        Long stickerId) {
        UserServiceUtil.existsUserById(userRepository, userId);
        LikeSchedule likeSchedule = likeScheduleRepository.findById(likeScheduleId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.UNREGISTERED_LIKE_SCHEDULE));
        StickerServiceUtil.existsStickerById(stickerRepository, stickerId);

        if (!likeSchedule.isLikeScheduleSender(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        likeSchedule.changeSticker(stickerId);

        return StickerActionResponse.builder()
            .likeScheduleId(likeSchedule.getId())
            .scheduleId(likeSchedule.getScheduleId())
            .senderId(likeSchedule.getSenderId())
            .stickerId(likeSchedule.getStickerId())
            .createdAt(likeSchedule.getCreatedAt())
            .updatedAt(likeSchedule.getUpdatedAt())
            .build();
    }

    @Transactional
    public List<ReceivedStickerResponse> getReceivedStickerList(Long userId, Long scheduleId) {
        UserServiceUtil.existsUserById(userRepository, userId);
        PillSchedule pillSchedule = PillScheduleServiceUtil.findPillScheduleById(
            pillScheduleRepository, scheduleId);
        Pill pill = PillServiceUtil.findPillById(pillRepository, pillSchedule.getPillId());

        if (!pill.isPillUser(userId) && !friendQueryRepository.isAlreadyFriend(userId,
            pill.getUserId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        return likeScheduleQueryRepository.getReceivedStickerList(scheduleId, userId);
    }

    @Transactional
    public void uploadStickerImage(MultipartFile stickerImage) {
        if (!stickerImage.isEmpty()) {
            String stickerImageUrl = s3Service.uploadImage(stickerImage, "sticker");
            stickerRepository.save(Sticker.builder().stickerImg(stickerImageUrl).build());
        }
    }
}
