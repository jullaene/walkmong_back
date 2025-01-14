package org.jullaene.walkmong_back.api.board.repository;

import org.jullaene.walkmong_back.api.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    boolean existsByOwnerIdAndBoardIdAndDelYn(Long ownerId, Long boardId, String delYn);
    @Query("SELECT b.ownerId FROM Board b WHERE b.boardId = :boardId")
    Long findOwnerIdByBoardId(Long boardId);

    Optional<Board> findByBoardIdAndDelYn(Long boardId, String delYn);
}
