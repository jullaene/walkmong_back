package org.jullaene.walkmong_back.api.apply.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplyInfoDto;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepository;
import org.jullaene.walkmong_back.api.board.domain.Board;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final BoardRepository boardRepository;
    private final DogRepository dogRepository;
    private final MemberService memberService;

    @Transactional
    public Long saveApply(Long boardId, ApplyRequestDto applyRequestDto) {
        Member member = memberService.getMemberFromUserDetail();

        // 본인이 쓴 게시글에는 지원 불가
        if (boardRepository.existsByBoardIdAndMemberIdAndDelYn(boardId, member.getMemberId(), "N")) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorType.CANNOT_SELF_APPLY);
        }

        // 한 게시글에 여러 번 지원 불가
        if (applyRepository.existsByBoardIdAndMemberIdAndDelYn(boardId, member.getMemberId(), "N")) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorType.CANNOT_DUPLICATED_APPLY);
        }

        Apply apply = Apply.builder()
                .memberId(member.getMemberId())
                .boardId(boardId)
                .applyRequestDto(applyRequestDto)
                .build();

        return applyRepository.save(apply).getApplyId();
    }

    @Transactional(readOnly = true)
    public void isValidWalkerByBoardIdAndMatchingStatus(Long memberId, Long boardId, MatchingStatus matchingStatus) {
        if (!applyRepository.existsByBoardIdAndMemberIdAndMatchingStatusAndDelYn(boardId, memberId, matchingStatus, "N")) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
        }
    }

    public ApplyInfoDto getApplyInfo(Long boardId) {
        Member member = memberService.getMemberFromUserDetail();

        // 게시글 id로 지원 내역 찾기
        Apply apply = applyRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.APPLY_NOT_FOUND));

        // 게시글의 강아지 아이디 찾기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.POST_NOT_FOUND));

        Dog dog = dogRepository.findById(board.getDogId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.DOG_NOT_FOUND));

        return createApplyInfoDto(member, apply, dog, board);


    }

    //산책자 노원구 공릉동, 30대 초반
    private ApplyInfoDto createApplyInfoDto(Member member, Apply apply, Dog dog, Board board) {
        return new ApplyInfoDto(
                dog.getName(),
                dog.getGender(),
                dog.getBreed(),
                dog.getDogSize(),
                member.getName(),
                member.getProfile(),
                member.getGender(),
                apply.getDongAddress(),
                board.getStartTime(),
                board.getEndTime(),
                apply.getAddressDetail(),
                apply.getMuzzleYn(),
                apply.getPoopBagYn(),
                apply.getPreMeetingYn(),
                apply.getMemoToOwner()
        );
    }
}
