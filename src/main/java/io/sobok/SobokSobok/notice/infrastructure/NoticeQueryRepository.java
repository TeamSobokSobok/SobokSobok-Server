package io.sobok.SobokSobok.notice.infrastructure;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.auth.domain.QUser;
import io.sobok.SobokSobok.friend.domain.QSendFriend;
import io.sobok.SobokSobok.notice.domain.QNotice;
import io.sobok.SobokSobok.notice.ui.dto.NoticeInfo;
import io.sobok.SobokSobok.pill.domain.QPill;
import io.sobok.SobokSobok.pill.domain.QSendPill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<NoticeInfo> getNoticeList(Long receiverId) {
        QUser user = QUser.user;
        QPill pill = QPill.pill;
        QNotice notice = QNotice.notice;
        QSendPill sendPill = QSendPill.sendPill;
        QSendFriend sendFriend = QSendFriend.sendFriend;

        return queryFactory
                .select(
                        Projections.constructor(
                                NoticeInfo.class,
                                notice.id.as("noticeId"),
                                notice.section,
                                notice.isOkay,
                                notice.createdAt,
                                user.username.as("senderName"),
                                pill.pillName,
                                pill.id.as("pillId"),
                                sendFriend.id.as("senderGroupId")
                        )
                )
                .from(notice)
                .leftJoin(sendPill).on(notice.id.eq(sendPill.noticeId))
                .leftJoin(sendFriend).on(notice.id.eq(sendFriend.noticeId))
                .leftJoin(user).on(notice.senderId.eq(user.id))
                .leftJoin(pill).on(sendPill.pillId.eq(pill.id))
                .where(notice.receiverId.eq(receiverId))
                .fetch();
    }
}
