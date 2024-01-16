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
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
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

    public void receivePill(Long userId) {
        this.userId = userId;
    }

    public boolean isPillUser(Long userId) {
        return Objects.equals(this.userId, userId);
    }
}
