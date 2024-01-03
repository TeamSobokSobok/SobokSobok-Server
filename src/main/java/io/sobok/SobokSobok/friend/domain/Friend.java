package io.sobok.SobokSobok.friend.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private String friendName;

    private Friend(Long senderId, Long receiverId, String friendName) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.friendName = friendName;
    }

    public static Friend newInstance(Long senderId, Long receiverId, String friendName) {
        return new Friend(senderId, receiverId, friendName);
    }
}
