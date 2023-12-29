package io.sobok.SobokSobok.pill.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class PillSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate scheduleDate;

    @Column(nullable = false)
    private String scheduleTime;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isCheck;

    @Column(nullable = false)
    private Long pillId;

    @Builder
    public PillSchedule(LocalDate scheduleDate, String scheduleTime, Long pillId) {
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.pillId = pillId;
    }
}
