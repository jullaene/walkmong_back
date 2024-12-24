package org.jullaene.walkmong_back.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.review.domain.enums.HashtagWalkerNm;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "hashtag_to_walker")
@Entity
@NoArgsConstructor
@DynamicUpdate
public class HashtagToWalker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_to_walker_id", nullable = false)
    private Long hashtagToWalkerId;

    @Comment("후기 아이디")
    private Long reviewToWalkerId;

    @Comment("후기 대상 아이디")
    private Long reviewTargetId;

    @Comment("해시태그 명")
    @Enumerated(EnumType.STRING)
    private HashtagWalkerNm hashtagWalkerNm;

    @Builder
    public HashtagToWalker(Long reviewToWalkerId, Long reviewTargetId, HashtagWalkerNm hashtagWalkerNm) {
        this.reviewToWalkerId = reviewToWalkerId;
        this.reviewTargetId = reviewTargetId;
        this.hashtagWalkerNm = hashtagWalkerNm;
    }
}
