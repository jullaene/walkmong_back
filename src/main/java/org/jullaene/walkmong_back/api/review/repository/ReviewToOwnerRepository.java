package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.domain.ReviewToOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewToOwnerRepository extends JpaRepository<ReviewToOwner, Long> {
    List<ReviewToOwner> findAllByDogIdAndDelYn(Long dogId, String delYn);
    List<ReviewToOwner> findAllByReviewTargetIdAndDelYn(Long reviewTargetId, String delYn);
}
