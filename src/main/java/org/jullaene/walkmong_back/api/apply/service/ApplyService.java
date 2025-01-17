package org.jullaene.walkmong_back.api.apply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.enums.WalkMatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.dto.res.*;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepository;
import org.jullaene.walkmong_back.api.board.domain.Board;
import org.jullaene.walkmong_back.api.board.domain.enums.WalkingStatus;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagResponseDto;
import org.jullaene.walkmong_back.api.review.dto.res.RatingResponseDto;
import org.jullaene.walkmong_back.api.review.service.ReviewToWalkerService;
import org.jullaene.walkmong_back.common.enums.TabStatus;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final ReviewToWalkerService reviewToWalkerService;

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

    @Transactional
    public ApplyInfoDto getApplyInfo(Long boardId) {
        Member member = memberService.getMemberFromUserDetail();
        return applyRepository.getApplyInfoResponse(boardId,member.getMemberId(),"N")
                .orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.INVALID_ADDRESS));
    }

    //전체 지원 내역 불러오기
    public List<MatchingResponseDto> getAllAppliedInfoWithStatus(TabStatus tabStatus, WalkMatchingStatus walkMatchingStatus) {
        Long memberId = memberService.getMemberFromUserDetail().getMemberId();

        List<MatchingResponseDto> matchingResponseDtos = new ArrayList<>();

        if (tabStatus.equals(TabStatus.APPLY) || tabStatus.equals(TabStatus.ALL)) {
            List<MatchingResponseDto> appliedLists = applyRepository.getApplyInfoResponses(memberId, walkMatchingStatus, "N");

            matchingResponseDtos.addAll(appliedLists);
        }

        if (tabStatus.equals(TabStatus.BOARD) || tabStatus.equals(TabStatus.ALL)) {
            List<MatchingResponseDto> boardLists = boardRepository.getBoardInfoResponse(memberId, walkMatchingStatus, "N");

            matchingResponseDtos.addAll(boardLists);
        }

        return matchingResponseDtos;

    }


    public List<ApplicantInfoResponseDto> getApplicantList(Long boardId) {
        return applyRepository.getApplicantList(boardId,"N");
    }

    //반려인이 산책자 지원서 조회
    public ApplicationFormResponseDto getApplicationFormInfo(Long boardId, Long applyId){
        //반려인의 아이디
        Long memberId=memberService.getMemberFromUserDetail().getMemberId();
        Apply apply=applyRepository.findById(applyId).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.POST_NOT_FOUND));
        Long walkerId=apply.getMemberId(); //산책자의 아이디

        BoardPreviewResponseDto boardDto = boardRepository.getBoardPreview(boardId,memberId,"N");
        ApplicantInfoResponseDto applicantDto = applyRepository.getApplicant(boardId,applyId,"N");
        ApplyInfoResponseDto applyDto = apply.toApplyInfoDto();
        RatingResponseDto ratingDto = reviewToWalkerService.calculateAverage(walkerId);
        List<HashtagResponseDto> hashtagDto = reviewToWalkerService.getTop3HashtagsByWalkerId(walkerId);

        ApplicationFormResponseDto responseDto = ApplicationFormResponseDto.builder()
                .boardDto(boardDto)
                .applicantDto(applicantDto)
                .applyDto(applyDto)
                .ratingDto(ratingDto)
                .hashtagDto(hashtagDto)
                .build();
        return responseDto;

    }

    /**
     매칭 확정 후 다른 요청자의 지원을 취소한다
     */
    @Transactional
    public void confirmMatching(Long boardId, Long applyId) {
        Apply matchApply = applyRepository.findByApplyIdAndBoardIdAndDelYn(applyId,boardId,"N");
        log.info("진입");
        matchApply.changeState(); //매칭 완료 상태로 바꾼다
        applyRepository.save(matchApply);

        Board board = boardRepository.findByBoardIdAndDelYn(matchApply.getBoardId(), "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_BOARD));

        board.updateWalkingStatus(WalkingStatus.BEFORE);

        //나머지 지원을 취소처리
        applyRepository.cancelOtherApplications(boardId,applyId);
    }


    /**
     * 지원자가 자신의 지원서를 조회한다
     */
    public MyFormResponseDto getMyForm(Long applyId) {
        Apply apply=applyRepository.findById(applyId).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.POST_NOT_FOUND));

        //산책 요청글의 boardID
        Long boardId=applyRepository.findIdByApplicantId(applyId);
        Board board=boardRepository.findById(boardId).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.INVALID_USER));

        BoardPreviewResponseDto previewDto=boardRepository.getBoardPreview(boardId,board.getOwnerId(),"N");

        MyFormResponseDto formResponseDto=MyFormResponseDto.builder()
                .previewResponseDto(previewDto)
                .applyInfoResponseDto(apply.toApplyInfoDto())
                .build();

        return formResponseDto;
    }

    /**
     지원자가 자신의 지원을 취소: delYn을 Y로 바꾼다
     */
    public void cancelApply(Long applyId) {
        Apply apply=applyRepository.findById(applyId).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.INVALID_USER));
        Apply changedApply=apply.cancelApply();
        applyRepository.save(changedApply);
    }

    /**
     *매칭 취소: status를 PENDING으로 바꾼다
     */
    public void cancelMatching(Long applyId) {
        Apply apply = applyRepository.findById(applyId).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.INVALID_USER));
        Apply changedApply = apply.cancelMatching();
        applyRepository.save(changedApply);

        Board board = boardRepository.findByBoardIdAndDelYn(apply.getBoardId(), "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_BOARD));

        board.updateWalkingStatus(WalkingStatus.PENDING);
    }

    /**
     * 매칭 확정 후 산책 정보 조회*/
    public WalkingDtlRes getWalkingDtlRes(Long boardId) {
        return applyRepository.getWalkingDtlRes(boardId, "N");
    }
}
