package io.sobok.SobokSobok.friend.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.external.firebase.FCMPushService;
import io.sobok.SobokSobok.external.firebase.dto.PushNotificationRequest;
import io.sobok.SobokSobok.friend.domain.Friend;
import io.sobok.SobokSobok.friend.domain.SendFriend;
import io.sobok.SobokSobok.friend.infrastructure.FriendQueryRepository;
import io.sobok.SobokSobok.friend.infrastructure.FriendRepository;
import io.sobok.SobokSobok.friend.infrastructure.SendFriendRepository;
import io.sobok.SobokSobok.friend.ui.dto.AddFriendRequest;
import io.sobok.SobokSobok.friend.ui.dto.AddFriendResponse;
import io.sobok.SobokSobok.friend.ui.dto.FriendListResponse;
import io.sobok.SobokSobok.friend.ui.dto.HandleFriendRequest;
import io.sobok.SobokSobok.friend.ui.dto.HandleFriendRequestResponse;
import io.sobok.SobokSobok.friend.ui.dto.UpdateFriendName;
import io.sobok.SobokSobok.friend.ui.dto.UpdateFriendNameResponse;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.domain.NoticeType;
import io.sobok.SobokSobok.notice.infrastructure.NoticeQueryRepository;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FCMPushService fcmPushService;

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final SendFriendRepository sendFriendRepository;
    private final FriendRepository friendRepository;
    private final NoticeQueryRepository noticeQueryRepository;
    private final FriendQueryRepository friendQueryRepository;

    @Transactional
    public AddFriendResponse addFriend(Long userId, AddFriendRequest request) {
        User sender = UserServiceUtil.findUserById(userRepository, userId);

        if (sender.getId().equals(request.memberId())) {
            throw new BadRequestException(ErrorCode.INVALID_SELF_ADD_FRIEND);
        }

        User receiver = UserServiceUtil.findUserById(userRepository, request.memberId());

        if (friendRepository.countBySenderId(sender.getId()) >= 5 ||
                friendRepository.countBySenderId(receiver.getId()) >= 5) {
            throw new ConflictException(ErrorCode.EXCEEDED_FRIEND_COUNT);
        }

        if (noticeQueryRepository.isAlreadyFriendRequestFromSender(sender.getId(), receiver.getId())
                || noticeQueryRepository.isAlreadyFriendRequestFromSender(receiver.getId(),
                sender.getId())) {
            throw new ConflictException(ErrorCode.ALREADY_FRIEND);
        }

        Notice notice = noticeRepository.save(
                Notice.newInstance(
                        sender.getId(),
                        receiver.getId(),
                        NoticeType.FRIEND,
                        NoticeStatus.WAITING
                )
        );

        sendFriendRepository.save(
                SendFriend.newInstance(
                        notice.getId(),
                        request.friendName()
                )
        );

        fcmPushService.sendNotificationByToken(PushNotificationRequest.builder()
                .userId(receiver.getId())
                .title(sender.getUsername() + "님이 친구를 신청했어요")
                .type("notice")
                .build());

        return AddFriendResponse.builder()
                .noticeId(notice.getId())
                .senderName(sender.getUsername())
                .memberName(receiver.getUsername())
                .isOkay(NoticeStatus.WAITING)
                .build();
    }

    @Transactional(readOnly = true)
    public List<FriendListResponse> getFriendList(Long userId) {
        UserServiceUtil.existsUserById(userRepository, userId);

        return friendRepository.findAllBySenderId(userId)
                .stream().map(friend ->
                        FriendListResponse.builder()
                                .friendId(friend.getId())
                                .memberId(friend.getReceiverId())
                                .friendName(friend.getFriendName())
                                .build()
                ).collect(Collectors.toList());
    }

    @Transactional(noRollbackFor = {ConflictException.class})
    public HandleFriendRequestResponse updateNoticeStatus(Long userId, Long noticeId,
                                                          HandleFriendRequest request) {
        UserServiceUtil.existsUserById(userRepository, userId);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST_EXCEPTION));

        if (!userId.equals(notice.getReceiverId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        User sender = UserServiceUtil.findUserById(userRepository, notice.getSenderId());
        User receiver = UserServiceUtil.findUserById(userRepository, userId);

        if (friendRepository.countBySenderId(userId) >= 5 ||
                friendRepository.countBySenderId(sender.getId()) >= 5) {
            notice.updateIsOkay(NoticeStatus.REFUSE);
            throw new ConflictException(ErrorCode.EXCEEDED_FRIEND_COUNT);
        }

        notice.updateIsOkay(request.isOkay());

        if (request.isOkay() == NoticeStatus.ACCEPT) {
            SendFriend sendFriend = sendFriendRepository.findByNoticeId(noticeId);
            friendRepository.save(Friend.newInstance(
                    sender.getId(),
                    userId,
                    sendFriend.getFriendName()
            ));

            friendRepository.save(Friend.newInstance(
                    userId,
                    sender.getId(),
                    sender.getUsername()
            ));

            fcmPushService.sendNotificationByTokenWithFriendData(PushNotificationRequest.builder()
                    .userId(sender.getId())
                    .title(receiver.getUsername() + "님이 친구를 수락했어요")
                    .type("share")
                    .data(Map.of("friendId", Long.toString(userId)))
                    .build());
        } else {
            fcmPushService.sendNotificationByToken(PushNotificationRequest.builder()
                    .userId(sender.getId())
                    .title(receiver.getUsername() + "님이 친구를 거절했어요")
                    .type("notice")
                    .build());
        }

        return HandleFriendRequestResponse.builder()
                .noticeId(notice.getId())
                .memberName(sender.getUsername())
                .isOkay(request.isOkay())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Transactional
    public UpdateFriendNameResponse updateFriendName(Long userId, Long friendId, UpdateFriendName request) {
        UserServiceUtil.existsUserById(userRepository, userId);

        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION));

        if (!friend.getSenderId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        friend.updateFriendName(request.friendName());

        return UpdateFriendNameResponse.builder()
                .friendId(friendId)
                .userId(userId)
                .memberId(friend.getReceiverId())
                .friendName(request.friendName())
                .build();
    }

    @Transactional
    public Boolean checkFriendRequest(Long userId, Long memberId) {

        boolean AlreadyFriendRequest = false;

        UserServiceUtil.existsUserById(userRepository, userId);
        UserServiceUtil.existsUserById(userRepository, memberId);

        if (friendQueryRepository.isAlreadyFriend(userId, memberId) || noticeRepository.existsBySenderIdAndReceiverIdAndIsOkay(userId, memberId, NoticeStatus.WAITING)) {
            AlreadyFriendRequest = true;
        }

        return AlreadyFriendRequest;
    }
}
