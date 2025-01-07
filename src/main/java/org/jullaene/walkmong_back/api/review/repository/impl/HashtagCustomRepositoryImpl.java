package org.jullaene.walkmong_back.api.review.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.review.domain.QHashtagToWalker;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagPercentageDto;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagResponseDto;
import org.jullaene.walkmong_back.api.review.repository.HashtagCustomRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HashtagCustomRepositoryImpl implements HashtagCustomRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<HashtagResponseDto> findTop3HashtagsByWalkerId(Long walkerId) {
        log.info("산책자 아이디:{}",walkerId);
        QHashtagToWalker hashtagToWalker=QHashtagToWalker.hashtagToWalker;
        return
                queryFactory.selectDistinct(
                                Projections.constructor(HashtagResponseDto.class,
                                      hashtagToWalker.hashtagWalkerNm.as("hashtag"),
                                      hashtagToWalker.hashtagToWalkerId.count().as("count")
                                ))
                        .from(hashtagToWalker)
                        .where(hashtagToWalker.reviewTargetId.eq(walkerId))
                        .groupBy(hashtagToWalker.hashtagWalkerNm)
                        .orderBy(hashtagToWalker.hashtagToWalkerId.count().desc())
                        .limit(3)
                        .fetch();
    }

    public List<HashtagPercentageDto> getTopHashtags(Long reviewTargetId, String delYn) {
        QHashtagToWalker hashtagToWalker = QHashtagToWalker.hashtagToWalker;

        return queryFactory
                .select(
                        Projections.constructor(
                                HashtagPercentageDto.class,
                                hashtagToWalker.hashtagWalkerNm,
                                Expressions.numberTemplate(Integer.class,
                                        "count({0}) * 100.0 / sum(count({0})) over()",
                                        hashtagToWalker.hashtagWalkerNm
                                ).as("percentage")
                        )
                )
                .from(hashtagToWalker)
                .where(hashtagToWalker.reviewTargetId.eq(reviewTargetId)
                        .and(hashtagToWalker.delYn.eq(delYn)))
                .groupBy(hashtagToWalker.hashtagWalkerNm)
                .orderBy(Expressions.numberTemplate(Long.class, "count({0})", hashtagToWalker.hashtagWalkerNm).desc())
                .limit(3)
                .fetch();
    }

}