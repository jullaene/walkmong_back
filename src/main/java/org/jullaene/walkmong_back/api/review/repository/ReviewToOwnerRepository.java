package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.domain.ReviewToOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewToOwnerRepository extends JpaRepository<ReviewToOwner, Long> {
}
