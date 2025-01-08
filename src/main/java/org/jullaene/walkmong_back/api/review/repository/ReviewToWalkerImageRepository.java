package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.domain.ReviewToWalkerImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewToWalkerImageRepository extends JpaRepository<ReviewToWalkerImage, Long>, ReviewToWalkerImageRepositoryCustom {
}
