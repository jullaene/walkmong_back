package org.jullaene.walkmong_back.api.apply.repository;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.*;

import java.util.List;
import java.util.Optional;

public interface ApplyRepositoryCustom {
    Optional<ApplyInfoDto> getApplyInfoResponse(Long boardId, Long memberId, String delYn);

    //내가 지원한 산책 리스트 조회
    List<AppliedInfoResponseDto> getApplyRecordResponse(Long memberId, MatchingStatus status,String delYn);
   List<ApplicantInfoResponseDto> getApplicantList(Long boardId, String delYn);
    ApplicantInfoResponseDto getApplicant(Long boardId,Long applyId, String delYn);
}
