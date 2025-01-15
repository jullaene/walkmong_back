package org.jullaene.walkmong_back.api.common.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MultiNotificationReq {
    private final List<Long> memberIds;
    private final String title;
    private final String body;

}
