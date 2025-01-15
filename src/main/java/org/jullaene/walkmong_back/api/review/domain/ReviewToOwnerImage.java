package org.jullaene.walkmong_back.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@Table(name = "review_to_owner_image")
@Entity
@NoArgsConstructor
@DynamicUpdate
public class ReviewToOwnerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_to_owner_image_id", nullable = false)
    private Long reviewToOwnerImageId;

    @Comment("반려인 후기 아이디")
    private Long reviewToOwnerId;

    @Comment("이미지")
    private String imageUrl;

    @Builder
    public ReviewToOwnerImage(Long reviewToOwnerId, String imageUrl) {
        this.reviewToOwnerId = reviewToOwnerId;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
