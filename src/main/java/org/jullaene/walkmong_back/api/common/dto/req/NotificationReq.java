package org.jullaene.walkmong_back.api.common.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationReq {
    private final Long memberId;
    private final String title;
    private final String body;
}
