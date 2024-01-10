package io.sobok.SobokSobok.friend.application;

import io.sobok.SobokSobok.auth.application.util.UserServiceUtil;
import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.exception.model.ForbiddenException;
import io.sobok.SobokSobok.friend.domain.Friend;
import io.sobok.SobokSobok.friend.domain.SendFriend;
import io.sobok.SobokSobok.friend.infrastructure.FriendRepository;
import io.sobok.SobokSobok.friend.infrastructure.SendFriendRepository;
import io.sobok.SobokSobok.friend.ui.dto.AddFriendResponse;
import io.sobok.SobokSobok.friend.ui.dto.HandleFriendRequestResponse;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.domain.NoticeType;
import io.sobok.SobokSobok.notice.infrastructure.NoticeQueryRepository;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final SendFriendRepository sendFriendRepository;
    private final FriendRepository friendRepository;
    private final NoticeQueryRepository noticeQueryRepository;

    @Transactional
    public AddFriendResponse addFriend(Long userId, Long memberId, String friendName) {
        User sender = UserServiceUtil.findUserById(userRepository, userId);

        if (sender.getId().equals(memberId)) {
            throw new BadRequestException(ErrorCode.INVALID_SELF_ADD_FRIEND);
        }

        User receiver = UserServiceUtil.findUserById(userRepository, memberId);

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
                friendName
            )
        );

        return AddFriendResponse.builder()
            .noticeId(notice.getId())
            .senderName(sender.getUsername())
            .memberName(receiver.getUsername())
            .isOkay(NoticeStatus.WAITING)
            .build();
    }

    @Transactional(noRollbackFor = {ConflictException.class})
    public HandleFriendRequestResponse updateNoticeStatus(Long userId, Long noticeId, NoticeStatus isOkay) {
        UserServiceUtil.existsUserById(userRepository, userId);

        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST_EXCEPTION));

        if (!userId.equals(notice.getReceiverId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        User sender = UserServiceUtil.findUserById(userRepository, notice.getSenderId());

        if (friendRepository.countBySenderId(userId) >= 5 ||
            friendRepository.countBySenderId(sender.getId()) >= 5) {
            notice.setIsOkay(NoticeStatus.REFUSE);
            throw new ConflictException(ErrorCode.EXCEEDED_FRIEND_COUNT);
        }

        notice.setIsOkay(isOkay);

        if (isOkay == NoticeStatus.ACCEPT) {
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
        }

        return HandleFriendRequestResponse.builder()
            .noticeId(notice.getId())
            .memberName(sender.getUsername())
            .isOkay(isOkay)
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
