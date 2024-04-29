package io.sobok.SobokSobok.auth.domain;

import io.sobok.SobokSobok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Embedded
    private SocialInfo socialInfo;

    @Column
    private String deviceToken;

    @Column(nullable = false)
    private String roles;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private String leaveReason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Builder
    public User(String username, SocialInfo socialInfo, String deviceToken, String roles, Platform platform) {
        this.username = username;
        this.socialInfo = socialInfo;
        this.deviceToken = deviceToken;
        this.roles = roles;
        this.platform = platform;
    }

    public void updateDeviceToken(String newDeviceToken) {
        this.deviceToken = newDeviceToken;
    }

    public void updatePlatform(Platform newPlatform) {
        this.platform = newPlatform;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void deleteUser(String leaveReason) {
        this.deviceToken = null;
        this.username = null;
        this.isDeleted = true;
        this.leaveReason = leaveReason;
        this.deletedAt = LocalDateTime.now();
        this.socialInfo.removeSocialInfo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
