package io.sobok.SobokSobok.sticker.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.friend.infrastructure.FriendQueryRepository;
import io.sobok.SobokSobok.pill.application.PillScheduleServiceUtil;
import io.sobok.SobokSobok.pill.application.PillServiceUtil;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import io.sobok.SobokSobok.pill.infrastructure.PillScheduleRepository;
import io.sobok.SobokSobok.sticker.domain.LikeSchedule;
import io.sobok.SobokSobok.sticker.infrastructure.LikeScheduleRepository;
import io.sobok.SobokSobok.sticker.infrastructure.StickerRepository;
import io.sobok.SobokSobok.sticker.ui.dto.SendStickerResponse;
import io.sobok.SobokSobok.sticker.ui.dto.StickerResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StickerService {

    private final StickerRepository stickerRepository;
    private final UserRepository userRepository;
    private final PillScheduleRepository pillScheduleRepository;
    private final PillRepository pillRepository;
    private final FriendQueryRepository friendQueryRepository;
    private final LikeScheduleRepository likeScheduleRepository;

    @Transactional
    public List<StickerResponse> getStickerList() {
        return stickerRepository.findAll().stream().map(
            sticker -> StickerResponse.builder()
                .stickerId(sticker.getId())
                .stickerImg(sticker.getStickerImg())
                .build()
        ).collect(Collectors.toList());
    }

    public SendStickerResponse sendSticker(Long userId, Long scheduleId, Long stickerId) {
        UserServiceUtil.existsUserById(userRepository, userId);
        PillSchedule pillSchedule = PillScheduleServiceUtil.findPillScheduleById(
            pillScheduleRepository, scheduleId);
        StickerServiceUtil.existsStickerById(stickerRepository, stickerId);

        if (!pillSchedule.getIsCheck()) {
            throw new BadRequestException(ErrorCode.UNCONSUMED_PILL);
        }

        Pill pill = PillServiceUtil.findPillById(pillRepository, pillSchedule.getPillId());
        Long receiverId = pill.getUserId();
        if (!friendQueryRepository.isAlreadyFriend(userId, receiverId)) {
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

        return SendStickerResponse.builder()
            .id(likeSchedule.getId())
            .scheduleId(likeSchedule.getScheduleId())
            .senderId(likeSchedule.getSenderId())
            .stickerId(likeSchedule.getStickerId())
            .createdAt(likeSchedule.getCreatedAt())
            .updatedAt(likeSchedule.getUpdatedAt())
            .build();
    }
}
