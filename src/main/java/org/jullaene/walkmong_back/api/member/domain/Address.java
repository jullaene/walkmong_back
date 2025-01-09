package org.jullaene.walkmong_back.api.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;
import org.jullaene.walkmong_back.api.member.dto.req.AddressReq;
import org.jullaene.walkmong_back.api.member.dto.res.AddressResponseDto;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "address")
@Entity
@DynamicUpdate
@NoArgsConstructor
public class Address extends BaseEntity {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Comment("사용자 아이디")
    private Long memberId;

    @Getter
    @Comment("위도")
    private Double latitude;

    @Getter
    @Comment("경도")
    private Double longitude;

    @Comment("도로명 주소")
    private String roadAddress;

    @Comment("동 주소")
    private String dongAddress;

    @Getter
    @Enumerated(EnumType.STRING)
    @Comment("거리 범위")
    private DistanceRange distanceRange;

    @Comment("기본 주소 유무")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String basicAddressYn;

    @Builder
    public Address(AddressReq addressReq, Long memberId) {
        this.memberId = memberId;
        this.latitude = addressReq.getLatitude();
        this.longitude = addressReq.getLongitude();
        this.roadAddress = addressReq.getRoadAddress();
        this.dongAddress = addressReq.getDongAddress();
        this.distanceRange = addressReq.getDistanceRange();
        this.basicAddressYn = addressReq.getBasicAddressYn();
    }

    public final AddressResponseDto toAddressResponseDto() {
        return AddressResponseDto.builder()
                .addressId(this.addressId)
                .dongAddress(this.dongAddress)
                .build();
    }

    public void changeBasicAddressYn(String basicAddressYn) {
        this.basicAddressYn = basicAddressYn;
    }
}
