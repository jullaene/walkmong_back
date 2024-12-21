package org.jullaene.walkmong_back.api.board.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    /**
     *
     * */
    @Transactional(readOnly = true)
    public void isValidOwnerByBoardIdAndDelYn (Long memberId, Long boardId, String delYn) {
       if (!boardRepository.existsByOwnerIdAndBoardIdAndDelYn(memberId, boardId, delYn)) {
           throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
       }
    }


}
