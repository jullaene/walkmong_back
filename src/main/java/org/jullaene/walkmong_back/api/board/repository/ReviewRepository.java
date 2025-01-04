package org.jullaene.walkmong_back.api.board.repository;

import org.jullaene.walkmong_back.api.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
