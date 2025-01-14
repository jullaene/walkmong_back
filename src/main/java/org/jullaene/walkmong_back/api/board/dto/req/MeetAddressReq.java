package org.jullaene.walkmong_back.api.board.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetAddressReq {
    private String dongAddress;
    private String roadAddress;
    private Double latitude;
    private Double longitude;
    private String addressDetail;
    private String addressMemo;
}
