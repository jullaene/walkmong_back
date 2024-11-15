package org.jullaene.walkmong_back.api.apply.domain;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "apply")
@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Apply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long applyId;

    @Comment("산책자 아이디")
    private Long memberId;

    @Comment("게시글 아이디")
    private Long boardId;

    @Comment("매칭 상태")
    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus;

    @Comment("위도")
    private Double latitude;

    @Comment("경도")
    private Double longitude;

    @Comment("도로명 주소")
    private String roadAddress;

    @Comment("동 주소")
    private String dongAddress;

    @Comment("주소 상세 정보")
    private String addressDetail;

    @Comment("주소 메모")
    private String addressMemo;

    @Comment("배변 봉투 필요 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String poopBagYn;

    @Comment("입마개 필요 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String muzzleYn;

    @Comment("리드줄 필요 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String dogCollarYn;

    @Comment("사전만남 진행 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String preMeetingYn;

    @Comment("반려인에게 전달할 메시지")
    private String memoToOwner;

    @Builder
    public Apply (Long memberId, Long boardId, ApplyRequestDto applyRequestDto) {
        this.memberId = memberId;
        this.boardId = boardId;
        matchingStatus = MatchingStatus.PENDING;
        dongAddress = applyRequestDto.getDongAddress();
        roadAddress = applyRequestDto.getRoadAddress();
        latitude = applyRequestDto.getLatitude();
        longitude = applyRequestDto.getLongitude();
        addressDetail = applyRequestDto.getAddressDetail();
        addressMemo = applyRequestDto.getAddressMemo();
        poopBagYn = applyRequestDto.getPoopBagYn();
        muzzleYn = applyRequestDto.getMuzzleYn();
        dogCollarYn = applyRequestDto.getDogCollarYn();
        preMeetingYn = applyRequestDto.getPreMeetingYn();
        memoToOwner = applyRequestDto.getMessageToOwner();
    }

}
