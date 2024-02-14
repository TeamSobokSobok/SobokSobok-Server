package io.sobok.SobokSobok.pill.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendPill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long noticeId;

    @Column(nullable = false)
    private Long pillId;

    private SendPill(Long noticeId, Long pillId) {
        this.noticeId = noticeId;
        this.pillId = pillId;
    }

    public static SendPill newInstance(Long noticeId, Long pillId) {
        return new SendPill(noticeId, pillId);
    }
}
