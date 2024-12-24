package org.jullaene.walkmong_back.api.member.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressResponseDto {
    private Long addressId;
    private String dongAddress;
}
