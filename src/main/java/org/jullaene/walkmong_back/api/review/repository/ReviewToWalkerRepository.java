package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.domain.ReviewToWalker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewToWalkerRepository extends JpaRepository<ReviewToWalker, Long> {
    List<ReviewToWalker> findAllByReviewTargetId(Long walkerId);


}
