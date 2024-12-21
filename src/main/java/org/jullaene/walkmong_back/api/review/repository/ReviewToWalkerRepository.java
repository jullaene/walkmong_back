package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.domain.ReviewToWalker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewToWalkerRepository extends JpaRepository<ReviewToWalker, Long> {
}
