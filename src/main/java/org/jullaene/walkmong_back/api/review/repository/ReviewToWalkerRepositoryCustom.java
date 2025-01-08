package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.dto.common.ReviewToWalkerBasicInfo;

import java.util.List;

public interface ReviewToWalkerRepositoryCustom {
    List<ReviewToWalkerBasicInfo> findAllByReviewTargetIdAndDelYn(Long reviewTargetId, String delYn);
}
