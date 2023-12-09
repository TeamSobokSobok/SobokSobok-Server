package io.sobok.SobokSobok.auth.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Embedded
    private SocialInfo socialInfo;

    @Column(nullable = false, unique = true)
    private String deviceToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    @Builder
    public User(String username, SocialInfo socialInfo, String deviceToken, Role role) {
        this.username = username;
        this.socialInfo = socialInfo;
        this.deviceToken = deviceToken;
        this.role = role;
    }

    public void updateDeviceToken(String newDeviceToken) {
        this.deviceToken = newDeviceToken;
    }
}
