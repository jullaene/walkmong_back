package org.jullaene.walkmong_back.api.apply.repository;

import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplicantListResponseDto;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplicantWithBoardResponseDto;
import org.jullaene.walkmong_back.api.apply.dto.res.AppliedInfoResponseDto;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplyInfoDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;

import java.util.List;
import java.util.Optional;

public interface ApplyRepositoryCustom {
    Optional<ApplyInfoDto> getApplyInfoResponse(Long boardId, Long memberId, String delYn);

    //지원한 산책의 채팅방 조회
    List<ChatRoomListResponseDto> getApplyChatList(Long memberId, MatchingStatus status);

    //내가 지원한 산책 리스트 조회
    List<AppliedInfoResponseDto> getApplyRecordResponse(Long memberId, MatchingStatus status,String delYn);

   List<ApplicantListResponseDto> getApplicantList(Long boardId, Long memberId, String delYn);
}
