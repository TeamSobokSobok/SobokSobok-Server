package io.sobok.SobokSobok.pill.infrastructure;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.pill.domain.QPill;
import io.sobok.SobokSobok.pill.domain.QPillSchedule;
import io.sobok.SobokSobok.pill.ui.dto.MonthScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<MonthScheduleResponse> getMonthSchedule(Long userId, LocalDate startDate, LocalDate endDate) {

        QPillSchedule pillSchedule = QPillSchedule.pillSchedule;
        QPill pill = QPill.pill;

        List<Tuple> results = queryFactory
                .select(pillSchedule.scheduleDate,
                        pillSchedule.id.count().as("scheduleCount"),
                        new CaseBuilder()
                                .when(pillSchedule.isCheck.isTrue())
                                .then(1)
                                .otherwise(0)
                                .sum()
                                .as("isCheckCount")
                )
                .from(pillSchedule)
                .leftJoin(pill).on(pill.id.eq(pillSchedule.pillId))
                .where(
                        pill.userId.eq(userId),
                        pillSchedule.scheduleDate.goe(startDate),
                        pillSchedule.scheduleDate.loe(endDate)
                )
                .groupBy(pillSchedule.scheduleDate)
                .fetch();

        return results.stream()
                .map(tuple -> {
                    LocalDate scheduleDate = tuple.get(pillSchedule.scheduleDate);
                    Long scheduleCount = Objects.requireNonNull(tuple.get(1, Number.class)).longValue();
                    Long isCheckCount = Objects.requireNonNull(tuple.get(2, Number.class)).longValue();

                    String isComplete = determineIsComplete(scheduleCount, isCheckCount);

                    return MonthScheduleResponse.builder()
                            .scheduleDate(scheduleDate)
                            .scheduleCount(scheduleCount)
                            .isCheckCount(isCheckCount)
                            .isComplete(isComplete)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String determineIsComplete(Long scheduleCount, Long isCheckCount) {
        if (scheduleCount.equals(isCheckCount)) {
            return "done";
        } else if (1 <= isCheckCount && isCheckCount < scheduleCount) {
            return "doing";
        } else {
            return "none";
        }
    }
}
