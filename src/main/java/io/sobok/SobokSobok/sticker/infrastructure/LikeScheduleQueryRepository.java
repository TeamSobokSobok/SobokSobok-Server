package io.sobok.SobokSobok.sticker.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.auth.domain.QUser;
import io.sobok.SobokSobok.sticker.domain.QLikeSchedule;
import io.sobok.SobokSobok.sticker.domain.QSticker;
import io.sobok.SobokSobok.sticker.ui.dto.ReceivedStickerResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeScheduleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ReceivedStickerResponse> getReceivedStickerList(Long scheduleId, Long userId) {
        QLikeSchedule likeSchedule = QLikeSchedule.likeSchedule;
        QSticker sticker = QSticker.sticker;
        QUser user = QUser.user;

        return queryFactory
            .select(
                Projections.constructor(
                    ReceivedStickerResponse.class,
                    likeSchedule.id.as("likeScheduleId"),
                    likeSchedule.scheduleId,
                    sticker.id.as("stickerId"),
                    sticker.stickerImg,
                    user.username,
                    user.id.eq(userId).as("isMySticker")
                )
            )
            .from(likeSchedule)
            .join(sticker).on(likeSchedule.stickerId.eq(sticker.id))
            .join(user).on(likeSchedule.senderId.eq(user.id))
            .where(likeSchedule.scheduleId.eq(scheduleId))
            .orderBy(likeSchedule.updatedAt.desc())
            .fetch();
    }

}
