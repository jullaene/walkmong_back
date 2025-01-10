package org.jullaene.walkmong_back.api.apply.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ApplyRequestDto {
    private String dongAddress;
    private String roadAddress;
    private Double latitude;
    private Double longitude;
    private String addressDetail;
    private String addressMemo;
    private String poopBagYn;
    private String muzzleYn;
    private String dogCollarYn;
    private String preMeetingYn;
    private String messageToOwner;
}
