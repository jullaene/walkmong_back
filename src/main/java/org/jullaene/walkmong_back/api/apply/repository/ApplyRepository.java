package org.jullaene.walkmong_back.api.apply.repository;

import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    boolean existsByBoardIdAndMemberIdAndDelYn(Long boardId, Long memberId, String delYn);
}
