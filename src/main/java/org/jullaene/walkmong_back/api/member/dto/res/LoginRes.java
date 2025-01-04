package org.jullaene.walkmong_back.api.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRes {
    private String accessToken;

    private String refreshToken;
}
