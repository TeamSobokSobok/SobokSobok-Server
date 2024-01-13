package io.sobok.SobokSobok.notice.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeType section;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeStatus isOkay;

    private Notice(Long senderId, Long receiverId, NoticeType section, NoticeStatus isOkay) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.section = section;
        this.isOkay = isOkay;
    }

    public static Notice newInstance(Long senderId, Long receiverId, NoticeType section, NoticeStatus isOkay) {
        return new Notice(senderId, receiverId, section, isOkay);
    }

    public Boolean isPillNotice() {
        return this.section == NoticeType.PILL;
    }

    public Boolean isCompleteNotice() {
        return this.isOkay.equals(NoticeStatus.ACCEPT) || this.isOkay.equals(NoticeStatus.REFUSE);
    }
}
