package org.jullaene.walkmong_back.api.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepository;
import org.jullaene.walkmong_back.api.board.domain.Board;
import org.jullaene.walkmong_back.api.board.dto.req.BoardRequestDto;
import org.jullaene.walkmong_back.api.board.dto.req.MeetAddressReq;
import org.jullaene.walkmong_back.api.board.dto.res.BoardDetailResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardResponseDto;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;
import org.jullaene.walkmong_back.api.member.repository.AddressRepository;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final AddressRepository addressRepository;
    private final MemberService memberService;
    private final DogRepository dogRepository;
    private final ApplyRepository applyRepository;

    /**
     * 게시글 리스트 조회
     * */
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards(LocalDate date, Long addressId, DistanceRange distance, DogSize dogSize, String matchingYn) {
        Member member = memberService.getMemberFromUserDetail();

        // 주소 id가 null이면 기본 주소 반환, 주소 id가 null이 아니면 해당 id를 가진 주소 반환
        Address address;
        if (addressId == null) {
            address = getBasicAddressAndDelYn(member.getMemberId(), "N");
            }
        else {
            address = getAddressByIdAndDelYn(addressId, "N");
        }
        return boardRepository.getBoardsWithFilters(date, address, distance, dogSize, matchingYn);
    }

    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoardDetail(Long boardId) {
        Member member = memberService.getMemberFromUserDetail();

        return boardRepository.getBoardDetailResponse(boardId,member.getMemberId(), "N")
                .orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,ErrorType.INVALID_ADDRESS));
    }

    @Transactional
    public Long createBoard(BoardRequestDto boardRequestDto) {
        Member member = memberService.getMemberFromUserDetail();

        List<Address> addresses = addressRepository.findByMemberIdAndDelYn(member.getMemberId(), "N");

        boolean isAddressValid = addresses.stream()
                .anyMatch(address -> address.getAddressId().equals(boardRequestDto.getAddressId()));

        if (!isAddressValid) {
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_ADDRESS);
        }

        Dog dog = dogRepository.findByDogIdAndDelYn(boardRequestDto.getDogId(), "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.DOG_NOT_FOUND));

        dog.updateWalkDetails(
                boardRequestDto.getWalkRequest(),
                boardRequestDto.getWalkNote(),
                boardRequestDto.getAdditionalRequest()
        );

        Board board = Board.builder()
                .boardRequestDto(boardRequestDto)
                .content(dog.getWalkRequestContent())
                .ownerId(member.getMemberId())
                .build();

        return boardRepository.save(board).getBoardId();
    }

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
    /**
     * 주어진 멤버가 가진 기본 address를 반환
     * */
    @Transactional(readOnly = true)
    protected Address getBasicAddressAndDelYn(Long memberId, String delYn) {
        return addressRepository.findByMemberIdAndBasicAddressYnAndDelYn(memberId,"Y", delYn)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_ADDRESS));
    }

    /**
     * 주어진 addressId를 이용하여 address 반환
     * */
    @Transactional(readOnly = true)
    protected Address getAddressByIdAndDelYn (Long addressId, String delYn) {

        return addressRepository.findByAddressIdAndDelYn(addressId, delYn)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_ADDRESS));

    }

    /**
     * 요청한 산책 채팅방 리스트 조회
     * */
    public List<ChatRoomListResponseDto> getAllChatListWithStatus(MatchingStatus status) {
        Long memberId = memberService.getMemberFromUserDetail().getMemberId();
        List<ChatRoomListResponseDto> chatList = boardRepository.getRequestChatList(memberId, status);
        return chatList;
    }

    /**
     * 매칭 현황 조회 시 산책 요청글 미리 보기
     */
    public BoardPreviewResponseDto getPreview(Long boardId){
        Long memberId=memberService.getMemberFromUserDetail().getMemberId();
        return boardRepository.getBoardPreview(boardId,memberId,"N");
    }

    /**
     * 만남 장소 변경
     * */
    @Transactional
    public String changeMeetAddress(Long boardId, MeetAddressReq meetAddressReq) {
        Member member = memberService.getMemberFromUserDetail();

        Board board = boardRepository.findByBoardIdAndDelYn(boardId, "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_BOARD));
        log.info("board 검색 완료 : " + board.getBoardId());

        Apply apply = applyRepository.findByBoardIdAndMatchingStatusAndDelYn(boardId, MatchingStatus.CONFIRMED, "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_APPLY));
        log.info("apply 검색 완료 : " + apply.getApplyId());

        if (!member.getMemberId().equals(board.getOwnerId()) && !member.getMemberId().equals(apply.getMemberId())) {
            log.error("요청한 유저 아이디 : {}, 반려인 아이디 : {}, 산책자 아이디 : {}", member.getMemberId(), board.getOwnerId(), apply.getMemberId());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ACCESS_DENIED);
        }
        log.info("유저 접근 인증 완료");

        apply.updateMeetAddress(meetAddressReq);

        return "SUCCESS";
    }
}
