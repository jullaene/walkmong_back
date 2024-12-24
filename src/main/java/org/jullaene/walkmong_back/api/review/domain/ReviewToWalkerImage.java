package org.jullaene.walkmong_back.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@Table(name = "review_to_walker_image")
@Entity
@NoArgsConstructor
@DynamicUpdate
public class ReviewToWalkerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_to_walker_image_id", nullable = false)
    private Long reviewToWalkerImageId;

    @Comment("산책자 후기 아이디")
    private Long reviewToWalkerId;

    @Comment("이미지")
    private String imageUrl;

    @Builder
    public ReviewToWalkerImage(Long reviewToWalkerId, String imageUrl) {
        this.reviewToWalkerId = reviewToWalkerId;
        this.imageUrl = imageUrl;
    }
}
