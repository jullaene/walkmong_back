package org.jullaene.walkmong_back.api.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    /**
     *  게시글 아이디와 삭제 여부로 해당 게시글의 유효한 반려인인지 확인
     * */
    @Transactional(readOnly = true)
    public void isValidOwnerByBoardIdAndDelYn (Long memberId, Long boardId, String delYn) {
        log.info("memberId : " + memberId + " boardId : " + boardId);
       if (!boardRepository.existsByOwnerIdAndBoardIdAndDelYn(memberId, boardId, delYn)) {
           throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
       }
    }


}
