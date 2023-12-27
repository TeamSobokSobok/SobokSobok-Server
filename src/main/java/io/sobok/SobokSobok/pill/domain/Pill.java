package io.sobok.SobokSobok.pill.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pillName;

    @Column(nullable = false)
    private Integer color;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String scheduleDay;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isStop;


    @Builder
    public Pill(String pillName, Integer color, LocalDate startDate, LocalDate endDate, String scheduleDay, Long userId) {
        this.pillName = pillName;
        this.color = color;
        this.startDate = startDate;
        this.endDate = endDate;
        this.scheduleDay = scheduleDay;
        this.userId = userId;
    }
}
