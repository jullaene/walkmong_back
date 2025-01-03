package org.jullaene.walkmong_back.api.apply.repository;

import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long>,ApplyRepositoryCustom {
    boolean existsByBoardIdAndMemberIdAndDelYn(Long boardId, Long memberId, String delYn);
    boolean existsByBoardIdAndMemberIdAndMatchingStatusAndDelYn(Long boardId, Long memberId, MatchingStatus matchingStatus, String delYn);
    Apply findByMemberIdAndBoardIdAndDelYn(Long walkerId,Long boardId,String delYn);

    @Modifying
    @Query("UPDATE Apply a SET a.matchingStatus = 'REJECTED' WHERE a.boardId = :boardId AND a.applyId <> :applyId")
    void cancelOtherApplications(Long boardId,Long applyId);
}
