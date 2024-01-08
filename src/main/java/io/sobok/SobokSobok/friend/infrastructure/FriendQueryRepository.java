package io.sobok.SobokSobok.friend.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.friend.domain.QFriend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Boolean isAlreadyFriend(Long senderId, Long receiverId) {
        QFriend friend = QFriend.friend;

        return queryFactory
            .selectFrom(friend)
            .where(
                friend.senderId.eq(senderId),
                friend.receiverId.eq(receiverId)
            ).fetchFirst() != null;
    }
}
