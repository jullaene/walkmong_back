package org.jullaene.walkmong_back.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToWalkerReqDto;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "review_to_walker")
@Entity
@NoArgsConstructor
@DynamicUpdate
@Getter
public class ReviewToWalker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_to_walker_id", nullable = false)
    private Long reviewToWalkerId;

    @Comment("게시글 아이디")
    private Long boardId;

    @Comment("후기 작성자 아이디")
    private Long reviewerId;

    @Comment("후기 대상자 아이디")
    private Long reviewTargetId;

    @Comment("후기")
    private String content;

    @Comment("시간 약속 준수 여부")
    private Float timePunctuality;

    @Comment("소통")
    private Float communication;

    @Comment("태도")
    private Float attitude;

    @Comment("산책 요청사항 이행")
    private Float taskCompletion;

    @Comment("산책 중 사진 공유")
    private Float photoSharing;

    @Builder
    public ReviewToWalker(ReviewToWalkerReqDto reviewToWalkerReqDto, Long reviewerId) {
        this.boardId = reviewToWalkerReqDto.getBoardId();
        this.reviewerId = reviewerId;
        this.reviewTargetId = reviewToWalkerReqDto.getWalkerId();
        this.content = reviewToWalkerReqDto.getContent();
        this.timePunctuality = reviewToWalkerReqDto.getTimePunctuality();
        this.communication = reviewToWalkerReqDto.getCommunication();
        this.attitude = reviewToWalkerReqDto.getAttitude();
        this.taskCompletion = reviewToWalkerReqDto.getTaskCompletion();
        this.photoSharing = reviewToWalkerReqDto.getPhotoSharing();
    }

    public Long getReviewToWalkerId () {
        return reviewToWalkerId;
    }
}
