package org.jullaene.walkmong_back.api.member.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;

@Getter
@AllArgsConstructor
public class AddressReq {
    private final String dongAddress;
    private final String roadAddress;
    private final Double latitude;
    private final Double longitude;
    private final DistanceRange distanceRange;
    private final String basicAddressYn;

    public Address toEntity(Long memberId) {
        return Address.builder()
                .addressReq(this)
                .memberId(memberId)
                .build();
    }
}
