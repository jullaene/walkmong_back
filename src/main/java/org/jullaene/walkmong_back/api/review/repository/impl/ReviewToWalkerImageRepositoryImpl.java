package org.jullaene.walkmong_back.api.review.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.review.domain.QReviewToWalker;
import org.jullaene.walkmong_back.api.review.domain.QReviewToWalkerImage;
import org.jullaene.walkmong_back.api.review.repository.ReviewToWalkerImageRepositoryCustom;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ReviewToWalkerImageRepositoryImpl implements ReviewToWalkerImageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<String>> findProfilesByReviewToWalkerIdsAndDelYn(List<Long> reviewIds, String delYn) {
        QReviewToWalker reviewToWalker = QReviewToWalker.reviewToWalker;
        QReviewToWalkerImage reviewToWalkerImage = QReviewToWalkerImage.reviewToWalkerImage;

        return queryFactory
                .select(
                        reviewToWalker.reviewToWalkerId,
                        reviewToWalkerImage.imageUrl
                )
                .from(reviewToWalkerImage)
                .join(reviewToWalker)
                .on(reviewToWalkerImage.reviewToWalkerId.eq(reviewToWalker.reviewToWalkerId))
                .where(reviewToWalker.reviewToWalkerId.in(reviewIds)
                        .and(reviewToWalker.delYn.eq(delYn)))
                .fetch()
                .stream()
                .filter(tuple -> Optional.ofNullable(tuple.get(0, Long.class)).isPresent())
                .collect(Collectors.groupingBy(
                        tuple -> Optional.ofNullable(tuple.get(0, Long.class))
                                .orElseThrow(() -> new RuntimeException("ReviewToWalkerId cannot be null")),
                        Collectors.mapping(
                                tuple -> tuple.get(1, String.class),
                                Collectors.toList()
                        )
                ));
    }
}
