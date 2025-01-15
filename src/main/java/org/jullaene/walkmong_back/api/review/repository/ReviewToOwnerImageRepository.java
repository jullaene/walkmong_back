package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.domain.ReviewToOwnerImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewToOwnerImageRepository extends JpaRepository<ReviewToOwnerImage, Long> {
    List<ReviewToOwnerImage> findAllByReviewToOwnerId(Long reviewerId);
}
