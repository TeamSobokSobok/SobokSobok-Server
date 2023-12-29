package io.sobok.SobokSobok.pill.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.pill.domain.QPill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PillQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Integer getPillCount(Long userId) {

        QPill pill = QPill.pill;

         return queryFactory
                .selectFrom(pill)
                .where(
                        pill.userId.eq(userId),
                        pill.isStop.eq(false)
                )
                .fetch()
                .size();
    }
}
