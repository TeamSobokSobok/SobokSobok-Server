package io.sobok.SobokSobok.friend.application;

import io.sobok.SobokSobok.auth.domain.User;
import io.sobok.SobokSobok.auth.infrastructure.UserRepository;
import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.exception.model.ConflictException;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.exception.model.UnauthorizedException;
import io.sobok.SobokSobok.friend.domain.Friend;
import io.sobok.SobokSobok.friend.domain.SendFriend;
import io.sobok.SobokSobok.friend.infrastructure.FriendQueryRepository;
import io.sobok.SobokSobok.friend.infrastructure.FriendRepository;
import io.sobok.SobokSobok.friend.infrastructure.SendFriendRepository;
import io.sobok.SobokSobok.friend.ui.dto.AddFriendResponse;
import io.sobok.SobokSobok.friend.ui.dto.HandleFriendRequestResponse;
import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.domain.NoticeType;
import io.sobok.SobokSobok.notice.infrastructure.NoticeRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendQueryRepository friendQueryRepository;
    private final NoticeRepository noticeRepository;
    private final SendFriendRepository sendFriendRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public AddFriendResponse addFriend(Long userId, Long memberId, String friendName) {
        User sender = validateUser(userId);

        if (sender.getId().equals(memberId)) {
            throw new BadRequestException(ErrorCode.INVALID_SELF_ADD_FRIEND);
        }

        User receiver = validateUser(memberId);

        if (friendQueryRepository.isAlreadyFriend(sender.getId(), receiver.getId())) {
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

    @Transactional
    public HandleFriendRequestResponse updateNoticeStatus(Long userId, Long noticeId, NoticeStatus isOkay) {
        validateUser(userId);

        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST_EXCEPTION));

        if (!userId.equals(notice.getReceiverId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        notice.setIsOkay(isOkay);

        User sender = validateUser(notice.getSenderId());
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

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.UNREGISTERED_USER));
    }
}
