package org.jullaene.walkmong_back.api.review.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.domain.QBoard;
import org.jullaene.walkmong_back.api.dog.domain.QDog;
import org.jullaene.walkmong_back.api.member.domain.QMember;
import org.jullaene.walkmong_back.api.review.domain.QReviewToWalker;
import org.jullaene.walkmong_back.api.review.dto.common.ReviewToWalkerBasicInfo;
import org.jullaene.walkmong_back.api.review.repository.ReviewToWalkerRepositoryCustom;

import java.util.List;

@RequiredArgsConstructor
public class ReviewToWalkerRepositoryImpl implements ReviewToWalkerRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<ReviewToWalkerBasicInfo> findAllByReviewTargetIdAndDelYn(Long reviewTargetId, String delYn) {
        QReviewToWalker reviewToWalker = QReviewToWalker.reviewToWalker;
        QBoard board = QBoard.board;
        QDog dog = QDog.dog;
        QMember member = QMember.member;

        return queryFactory
                .select(
                        Projections.constructor(
                                ReviewToWalkerBasicInfo.class,
                                reviewToWalker.reviewToWalkerId,
                                member.nickname,
                                member.profile,
                                dog.name,
                                board.startTime,
                                reviewToWalker.photoSharing,
                                reviewToWalker.attitude,
                                reviewToWalker.taskCompletion,
                                reviewToWalker.timePunctuality,
                                reviewToWalker.communication,
                                reviewToWalker.content
                        )
                )
                .from(reviewToWalker)
                .join(member)
                .on(member.memberId.eq(reviewToWalker.reviewerId)
                        .and(member.delYn.eq(delYn)))
                .join(board)
                .on(board.boardId.eq(reviewToWalker.boardId)
                        .and(board.delYn.eq(delYn)))
                .join(dog)
                .on(dog.dogId.eq(board.dogId)
                        .and(dog.memberId.eq(reviewToWalker.reviewerId))
                        .and(dog.delYn.eq(delYn)))
                .where(reviewToWalker.reviewTargetId.eq(reviewTargetId)
                        .and(reviewToWalker.delYn.eq(delYn)))
                .fetch();
    }
}
