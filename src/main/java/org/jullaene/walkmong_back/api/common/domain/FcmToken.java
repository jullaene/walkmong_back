package org.jullaene.walkmong_back.api.common.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jullaene.walkmong_back.common.BaseEntity;

@Entity
@Table(name = "fcm_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fcmTokenId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String token;

    public FcmToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public Long getFcmTokenId () {
        return this.fcmTokenId;
    }

    public String getToken () {
        return this.token;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
