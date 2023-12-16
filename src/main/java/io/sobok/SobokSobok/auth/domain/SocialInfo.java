package io.sobok.SobokSobok.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialInfo {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false)
    private String socialId;

    @Column
    private String socialProfileImage;

    @Builder
    public SocialInfo(SocialType socialType, String socialId, String socialProfileImage) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.socialProfileImage = socialProfileImage;
    }
}
