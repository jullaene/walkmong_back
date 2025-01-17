package org.jullaene.walkmong_back.api.board.repository;

import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.enums.WalkMatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.MatchingResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardDetailResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardResponseDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    List<BoardResponseDto> getBoardsWithFilters(LocalDate date, Address walkerAddress, DistanceRange distance, DogSize dogSize, String matchingYn);
    boolean existsByBoardIdAndMemberIdAndDelYn(Long boardId, Long memberId, String delYn);
    Optional<BoardDetailResponseDto> getBoardDetailResponse(Long boardId, Long memberId, String delYn);

    //지원한 산책의 채팅방 조회
    List<ChatRoomListResponseDto> getRequestChatList(Long memberId, MatchingStatus status);
    BoardPreviewResponseDto getBoardPreview(Long boardId, Long memberId, String delYn);

    List<MatchingResponseDto> getBoardInfoResponse(Long memberId, WalkMatchingStatus walkMatchingStatus, String delYn);
}
