package org.jullaene.walkmong_back.api.review.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.review.domain.QHashtagToWalker;
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
}
