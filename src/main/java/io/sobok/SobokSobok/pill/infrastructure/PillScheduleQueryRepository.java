package io.sobok.SobokSobok.pill.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.pill.domain.QPillSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PillScheduleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<String> getPillScheduleTime(Long pillId) {

        QPillSchedule pillSchedule = QPillSchedule.pillSchedule;

        return queryFactory
                .select(pillSchedule.scheduleTime)
                .distinct()
                .from(pillSchedule)
                .where(pillSchedule.pillId.eq(pillId))
                .fetch();
    }
}
