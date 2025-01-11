package org.jullaene.walkmong_back.api.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser; // 새로운 필드 추가
}
