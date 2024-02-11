package io.sobok.SobokSobok.pill.infrastructure;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sobok.SobokSobok.pill.domain.PillSchedule;
import io.sobok.SobokSobok.pill.domain.QPill;
import io.sobok.SobokSobok.pill.domain.QPillSchedule;
import io.sobok.SobokSobok.pill.ui.dto.DateScheduleResponse;
import io.sobok.SobokSobok.pill.ui.dto.MonthScheduleResponse;
import io.sobok.SobokSobok.pill.ui.dto.PillScheduleInfo;
import io.sobok.SobokSobok.sticker.domain.QLikeSchedule;
import io.sobok.SobokSobok.sticker.domain.QSticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
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

    public List<DateScheduleResponse> getDateSchedule(Long userId, LocalDate date) {

        QPillSchedule pillSchedule = QPillSchedule.pillSchedule;
        QPill pill = QPill.pill;
        QSticker sticker = QSticker.sticker;
        QLikeSchedule likeSchedule = QLikeSchedule.likeSchedule;

        List<String> pillScheduleTimeList = queryFactory
                .select(pillSchedule.scheduleTime)
                .distinct()
                .from(pillSchedule)
                .leftJoin(pill).on(pill.id.eq(pillSchedule.pillId))
                .where(pill.userId.eq(userId), pillSchedule.scheduleDate.eq(date))
                .fetch();

        List<DateScheduleResponse> dateScheduleResponses = new ArrayList<>();

        for (String time : pillScheduleTimeList) {
            List<Long> pillScheduleIds = queryFactory
                    .select(pillSchedule.id)
                    .from(pillSchedule)
                    .where(pillSchedule.scheduleDate.eq(date), pillSchedule.scheduleTime.eq(time))
                    .fetch();

            Map<Long, List<Long>> stickerIdMap = pillScheduleIds.stream()
                    .collect(Collectors.toMap(id -> id, id -> queryFactory
                            .select(likeSchedule.stickerId)
                            .from(likeSchedule)
                            .where(likeSchedule.scheduleId.eq(id))
                            .fetch()));

            // 결과 매핑
            List<PillScheduleInfo> pillScheduleInfoList = pillScheduleIds.stream()
                    .flatMap(scheduleId -> {
                        List<Long> stickerIds = stickerIdMap.getOrDefault(scheduleId, Collections.emptyList());
                        return queryFactory
                                .select(
                                        pillSchedule.id,
                                        pill.id,
                                        pill.pillName,
                                        pillSchedule.isCheck,
                                        pill.color,
                                        likeSchedule.scheduleId.count()
                                )
                                .from(pillSchedule)
                                .leftJoin(pill).on(pill.id.eq(pillSchedule.pillId))
                                .leftJoin(likeSchedule).on(likeSchedule.scheduleId.eq(pillSchedule.id))
                                .where(pillSchedule.id.eq(scheduleId))
                                .groupBy(pillSchedule.id, pill.id, pill.pillName, pillSchedule.isCheck, pill.color)
                                .fetch()
                                .stream()
                                .map(tuple -> PillScheduleInfo.builder()
                                        .scheduleId(tuple.get(0, Long.class))
                                        .pillId(tuple.get(1, Long.class))
                                        .pillName(tuple.get(2, String.class))
                                        .isCheck(tuple.get(3, Boolean.class))
                                        .color(tuple.get(4, Integer.class))
                                        .stickerId(stickerIds)
                                        .stickerTotalCount(tuple.get(5, Long.class))
                                        .build()
                                );
                    }).collect(Collectors.toList());

            dateScheduleResponses.add(new DateScheduleResponse(time, pillScheduleInfoList));
        }

        return dateScheduleResponses;
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
