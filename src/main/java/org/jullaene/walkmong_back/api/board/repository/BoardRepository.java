package org.jullaene.walkmong_back.api.board.repository;

import org.jullaene.walkmong_back.api.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    boolean existsByOwnerIdAndBoardIdAndDelYn(Long ownerId, Long boardId, String delYn);
    List<Board> findByOwnerId(Long memberId);
}
