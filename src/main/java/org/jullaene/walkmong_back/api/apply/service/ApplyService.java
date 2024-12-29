package org.jullaene.walkmong_back.api.apply.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    @Transactional(readOnly = true)
    public void isValidWalkerByBoardIdAndMatchingStatus(Long memberId, Long boardId, MatchingStatus matchingStatus) {
        if (!applyRepository.existsByBoardIdAndMemberIdAndMatchingStatusAndDelYn(boardId, memberId, matchingStatus, "N")) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
        }
    }
}
