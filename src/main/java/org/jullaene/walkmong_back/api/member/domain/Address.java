package org.jullaene.walkmong_back.api.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "address")
@Entity
@DynamicUpdate
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Comment("사용자 아이디")
    private Long memberId;

    @Comment("위도")
    private Double latitude;

    @Comment("경도")
    private Double longitude;

    @Comment("도로명 주소")
    private String roadAddress;

    @Comment("동 주소")
    private String dongAddress;

    @Comment("거리 범위")
    private DistanceRange distanceRange;

    @Comment("기본 주소 유무")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String basicAddressYn;

}
