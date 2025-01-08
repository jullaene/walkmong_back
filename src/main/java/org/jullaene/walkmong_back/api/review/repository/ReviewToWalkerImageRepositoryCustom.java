package org.jullaene.walkmong_back.api.review.repository;

import java.util.List;
import java.util.Map;

public interface ReviewToWalkerImageRepositoryCustom {
    Map<Long, List<String>> findProfilesByReviewToWalkerIdsAndDelYn(List<Long> reviewIds, String delYn);
}
