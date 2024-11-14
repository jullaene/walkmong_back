package org.jullaene.walkmong_back.api.request.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.request.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "request")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Request extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Comment("산책자 아이디")
    private Long memberId;

    @Comment("게시글 아이디")
    private Long boardId;

    @Comment("매칭 상태")
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
    private String nuzzleYn;

    @Comment("리드줄 필요 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String dogCollarYn;

    @Comment("사전만남 진행 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    private String preMeetingYn;

    @Comment("반려인에게 전달할 메시지")
    private String memoToOwner;
}
