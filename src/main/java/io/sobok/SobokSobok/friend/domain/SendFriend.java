package io.sobok.SobokSobok.friend.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendFriend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long noticeId;

    @Column(nullable = false)
    private String friendName;

    private SendFriend(Long noticeId, String friendName) {
        this.noticeId = noticeId;
        this.friendName = friendName;
    }

    public static SendFriend newInstance(Long noticeId, String friendName) {
        return new SendFriend(noticeId, friendName);
    }
}
