package org.jullaene.walkmong_back.api.board.repository;

import org.jullaene.walkmong_back.api.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    boolean existsByOwnerIdAndBoardIdAndDelYn(Long ownerId, Long boardId, String delYn);
}
