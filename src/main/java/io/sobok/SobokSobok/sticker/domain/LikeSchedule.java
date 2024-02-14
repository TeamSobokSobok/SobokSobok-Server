package io.sobok.SobokSobok.sticker.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long stickerId;

    @Builder
    public LikeSchedule(Long scheduleId, Long senderId, Long stickerId) {
        this.scheduleId = scheduleId;
        this.senderId = senderId;
        this.stickerId = stickerId;
    }

    public void changeSticker(Long stickerId) {
        this.stickerId = stickerId;
    }

    public Boolean isLikeScheduleSender(Long userId) {
        return Objects.equals(senderId, userId);
    }
}
