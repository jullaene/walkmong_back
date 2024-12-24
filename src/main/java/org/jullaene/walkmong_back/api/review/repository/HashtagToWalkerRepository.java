package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.domain.HashtagToWalker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagToWalkerRepository extends JpaRepository<HashtagToWalker, Long> {
}
