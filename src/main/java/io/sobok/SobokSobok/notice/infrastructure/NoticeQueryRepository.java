package io.sobok.SobokSobok.notice.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import io.sobok.SobokSobok.notice.domain.NoticeType;
import io.sobok.SobokSobok.notice.domain.QNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Boolean isAlreadyFriendRequestFromSender(Long senderId, Long receiverId) {
        QNotice notice = QNotice.notice;

        return queryFactory
            .selectFrom(notice)
            .where(
                notice.senderId.eq(senderId),
                notice.receiverId.eq(receiverId),
                notice.section.eq(NoticeType.FRIEND),
                notice.isOkay.ne(NoticeStatus.REFUSE)
            ).fetchFirst() != null;

    }
}
