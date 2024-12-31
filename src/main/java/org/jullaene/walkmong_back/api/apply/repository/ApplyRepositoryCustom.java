package org.jullaene.walkmong_back.api.apply.repository;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.AppliedInfoResponseDto;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplyInfoDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;

import java.util.List;
import java.util.Optional;

public interface ApplyRepositoryCustom {
    Optional<ApplyInfoDto> getApplyInfoResponse(Long boardId, Long memberId, String delYn);
    List<AppliedInfoResponseDto> getApplyRecordResponse(Long memberId, MatchingStatus status);

    //지원한 산책의 채팅방 조회
    List<ChatRoomListResponseDto> getApplyChatList(Long memberId, MatchingStatus status);



}
