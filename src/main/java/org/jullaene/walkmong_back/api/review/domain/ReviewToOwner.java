package org.jullaene.walkmong_back.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.review.domain.enums.Activity;
import org.jullaene.walkmong_back.api.review.domain.enums.Aggressiveness;
import org.jullaene.walkmong_back.api.review.domain.enums.Sociality;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToOwnerReqDto;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "review_to_owner")
@Entity
@NoArgsConstructor
@DynamicUpdate
public class ReviewToOwner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_to_owner_id", nullable = false)
    private Long reviewToOwnerId;

    @Comment("게시글 아이디")
    private Long boardId;

    @Comment("후기 작성자 아이디")
    private Long reviewerId;

    @Comment("후기 대상자 아이디")
    private Long reviewTargetId;

    @Comment("후기")
    private String content;

    @Comment("좋음/싫음")
    @Column(name = "good_yn", columnDefinition = "VARCHAR(1) default 'N'")
    private String goodYn;

    @Comment("사회성")
    @Enumerated(EnumType.STRING)
    private Sociality sociality;

    @Comment("활동량")
    @Enumerated(EnumType.STRING)
    private Activity activity;

    @Comment("공격력")
    @Enumerated(EnumType.STRING)
    private Aggressiveness aggressiveness;

    @Comment("아쉬운 점")
    private String disappointment;

    @Builder
    public ReviewToOwner(ReviewToOwnerReqDto reviewToOwnerReqDto, Long reviewerId) {
        this.boardId = reviewToOwnerReqDto.getBoardId();
        this.reviewerId = reviewerId;
        this.reviewTargetId = reviewToOwnerReqDto.getOwnerId();
        this.goodYn = reviewToOwnerReqDto.getGoodYn();
        this.content = reviewToOwnerReqDto.getContent();
        this.sociality = reviewToOwnerReqDto.getSociality();
        this.activity = reviewToOwnerReqDto.getActivity();
        this.aggressiveness = reviewToOwnerReqDto.getAggressiveness();
        this.disappointment = reviewToOwnerReqDto.getDisappointment();
    }

    public Long getReviewToOwnerId () {
        return reviewToOwnerId;
    }
}
