package org.jullaene.walkmong_back.api.board.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.dto.res.BoardRes;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;
import org.jullaene.walkmong_back.api.member.repository.AddressRepository;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final AddressRepository addressRepository;
    private final MemberService memberService;

    /**
     * 게시글 리스트 조회
     * */
    public List<BoardRes> getBoards(LocalDate date, Long addressId, DistanceRange distance, DogSize dogSize, String matchingYn) {
        Member member = memberService.getMemberFromUserDetail();

        // 주소 id가 null이면 기본 주소 반환, 주소 id가 null이 아니면 해당 id를 가진 주소 반환
        Address address;
        if (addressId == null) {
            address = addressRepository.findByMemberIdAndBasicAddressYn(member.getMemberId(), "Y")
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_ADDRESS));
        }
        else {
            address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_ADDRESS));
        }

        return boardRepository.getBoardsWithFilters(date, address, distance, dogSize, matchingYn);
    }
}
