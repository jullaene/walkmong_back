package org.jullaene.walkmong_back.api.apply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.dto.res.*;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepository;
import org.jullaene.walkmong_back.api.board.domain.Board;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.repository.MemberRepository;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.api.review.dto.res.RatingResponseDto;
import org.jullaene.walkmong_back.api.review.service.ReviewToWalkerService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final BoardRepository boardRepository;
    private final DogRepository dogRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
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
    public List<RecordResponseDto> getAllAppliedInfoWithStatus(MatchingStatus status) {
        Long memberId=memberService.getMemberFromUserDetail().getMemberId();
        List<AppliedInfoResponseDto> appliedLists=applyRepository.getApplyRecordResponse(memberId,status,"N");

        return appliedLists.stream().map(dto-> (RecordResponseDto) dto).toList();

    }

    //지원한 산책의 채팅방 조회
    public List<ChatRoomListResponseDto> getAllChatListWithStatus(MatchingStatus status) {
        Long memberId = memberService.getMemberFromUserDetail().getMemberId();
        log.info("사용자 id {}", memberId);
        List<ChatRoomListResponseDto> chatList = applyRepository.getApplyChatList(memberId, status);
        return chatList;
    }

    public List<ApplicantListResponseDto> getApplicantList(Long boardId) {
        Long memberId=memberService.getMemberFromUserDetail().getMemberId();

        return applyRepository.getApplicantList(boardId,memberId,"N");
    }

    //반려인이 산책자 지원서 조회
    public ApplicationFormResponseDto getApplicationFormInfo(Long boardId, Long walkerId){
        //반려인의 아이디
        Long memberId=memberService.getMemberFromUserDetail().getMemberId();
        BoardPreviewResponseDto boardDto=boardRepository.getBoardPreview(boardId,memberId,"N");
        WalkerInfoResponseDto walkerDto=applyRepository.getApplicantInfo(boardId,walkerId);
        RatingResponseDto ratingDto=reviewToWalkerService.calculateAverage(walkerId);

        ApplicationFormResponseDto responseDto= ApplicationFormResponseDto.builder()
                .boardDto(boardDto)
                .walkerDto(walkerDto)
                .ratingDto(ratingDto)
                .build();
        return responseDto;

    }

    /**
     매칭 확정 후 다른 요청자의 지원을 취소한다
     */
    @Transactional
    public void confirmMatching(Long boardId, Long walkerId) {
        Apply matchApply=applyRepository.findByMemberIdAndBoardIdAndDelYn(walkerId,boardId,"N");
        matchApply.changeState(); //매칭 완료 상태로 바꾼다
        applyRepository.save(matchApply);
        //나머지 지원을 취소처리
        applyRepository.cancelOtherApplications(boardId,walkerId);
    }


    /**
     * 지원자가 자신의 지원서를 조회한다
     */
    public  MyFormResponseDto getMyForm(Long applyId) {
        Apply apply=applyRepository.findById(applyId).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.POST_NOT_FOUND));

        //지원자의 아이디
        Long applicantId=memberService.getMemberFromUserDetail().getMemberId();
        //산책 요청글의 boardID
        Long boardId=applyRepository.findIdByApplicantId(applyId);
        Board board=boardRepository.findById(boardId).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.INVALID_USER));

        BoardPreviewResponseDto preivewDto=boardRepository.getBoardPreview(boardId,board.getOwnerId(),"N");

        MyFormResponseDto formResponseDto=MyFormResponseDto.builder().
                previewResponseDto(preivewDto).
                dongAddress(apply.getDongAddress())
                .roadAddress(apply.getRoadAddress())
                .addressDetail(apply.getAddressDetail())
                .addressMemo(apply.getAddressMemo())
                .poopBagYn(apply.getPoopBagYn())
                .muzzleYn(apply.getMuzzleYn())
                .dogCollarYn(apply.getDogCollarYn())
                .preMeetingYn(apply.getPreMeetingYn())
                .memoToOwner(apply.getMemoToOwner())
                .build();

        return formResponseDto;
    }
}
