package org.jullaene.walkmong_back.api.apply.repository;

import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyRepositoryCustom {
    boolean existsByBoardIdAndMemberIdAndDelYn(Long boardId, Long memberId, String delYn);
    boolean existsByBoardIdAndMemberIdAndMatchingStatusAndDelYn(Long boardId, Long memberId, MatchingStatus matchingStatus, String delYn);


    @Modifying
    @Query("UPDATE Apply a SET a.matchingStatus = 'REJECTED' WHERE a.boardId = :boardId AND a.applyId <> :applyId")
    void cancelOtherApplications(Long boardId,Long applyId);

    //산책 지원글로부터 요청자 아이디를 반환한다
    @Query("SELECT a.boardId FROM Apply a WHERE a.applyId = :applyId")
    Long findIdByApplicantId(Long applyId);

    Apply findByApplyIdAndBoardIdAndDelYn(Long applyId, Long boardId, String n);

    Optional<Apply> findByBoardIdAndMatchingStatusAndDelYn(Long boardId, MatchingStatus matchingStatus, String delYn);
}
