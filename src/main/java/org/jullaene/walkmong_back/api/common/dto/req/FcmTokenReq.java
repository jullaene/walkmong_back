package org.jullaene.walkmong_back.api.common.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmTokenReq {
    private final Long memberId;
    private final String token;
}
