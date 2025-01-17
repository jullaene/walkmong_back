package org.jullaene.walkmong_back.api.member.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.jullaene.walkmong_back.api.dog.service.DogService;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.common.WalkingBasicInfo;
import org.jullaene.walkmong_back.api.member.dto.req.MemberAdditionalInfoRequestDto;
import org.jullaene.walkmong_back.api.member.dto.req.MemberReqDto;
import org.jullaene.walkmong_back.api.member.dto.req.WalkExperienceReq;
import org.jullaene.walkmong_back.api.member.dto.res.MemberResponseDto;
import org.jullaene.walkmong_back.api.member.dto.res.MyInfoResponseDto;
import org.jullaene.walkmong_back.api.member.dto.res.WalkingResponseDto;
import org.jullaene.walkmong_back.api.member.repository.AddressRepository;
import org.jullaene.walkmong_back.api.member.repository.MemberRepository;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagPercentageDto;
import org.jullaene.walkmong_back.api.review.repository.HashtagToWalkerRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.file.FileService;
import org.jullaene.walkmong_back.common.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jullaene.walkmong_back.common.exception.ErrorType.INVALID_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final HashtagToWalkerRepository hashtagToWalkerRepository;
    private final FileService fileService;
    private final DogRepository dogRepository;

    /**
     * CustomUserDetail에서 member 가져오기
     * */
    public Member getMemberFromUserDetail () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail customUserDetail) {
            return customUserDetail.getMember(); // member 객체 가져오기
        }
        throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.USER_NOT_AUTHENTICATED);
    }

    @Transactional
    public Long registerWalkingExperience(@Valid WalkExperienceReq walkExperienceReq) {
        // token으로 멤버 체크
        Member memberForCheck = getMemberFromUserDetail();

        // 실제 멤버를 db에서 가져오기
        Member member = memberRepository.findById(memberForCheck.getMemberId())
                        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_USER));

        member.addWalkingExperience(walkExperienceReq);

        return member.getMemberId();
    }

    /**
     * 사용자의 기본 정보 조회
     * */
    @Transactional(readOnly = true)
    public MyInfoResponseDto getMyInfo() {
        Member member = getMemberFromUserDetail();

        return memberRepository.getMemberInfo(member.getMemberId(), "N");
    }

    /**
     * 사용자의 기본 정보 수정
     * */
    @Transactional
    public Long updateMemberInfo(MemberReqDto memberReqDto) {
        Member authMember = getMemberFromUserDetail();
        Member member = memberRepository.findByMemberIdAndDelYn(authMember.getMemberId(), "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, INVALID_USER));

        // 멤버 정보 저장
        String profileUrl = fileService.uploadFile(memberReqDto.getProfile(), "member");
        member.update(memberReqDto, profileUrl);

        // 입력된 주소를 기본 주소로 저장

        if (!addressRepository.existsByAddressIdAndMemberIdAndDelYn(memberReqDto.getAddressId(), member.getMemberId(), "N")) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorType.ACCESS_DENIED);
        }

        List<Address> addresses = addressRepository.findByMemberIdAndDelYn(member.getMemberId(), "N");

        // 모든 주소 다 기본에서 제외
        addresses.forEach(address -> address.changeBasicAddressYn("N"));

        // 주어진 addressId를 기본 주소로 설정
        addresses.stream()
                .filter(address -> address.getAddressId().equals(address.getAddressId()))
                .forEach(address -> address.changeBasicAddressYn("Y"));

        return member.getMemberId();
    }

    /**
     * 유저의 산책 관련 정보 조회
     * */
    @Transactional(readOnly = true)
    public WalkingResponseDto getWalkingInfo() {
        Member member = getMemberFromUserDetail();

        return getWalkingResponseDto(member.getMemberId());
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo (Long memberId) {
        WalkingResponseDto walkingResponseDto = getWalkingResponseDto(memberId);
        List<Dog> dogs = dogRepository.findByMemberIdAndDelYn(memberId, "N");

        List<DogProfileResponseDto> dogProfileResponseDtos =  dogs.stream()
                .map(Dog::toDogProfileResponseDto)
                .collect(Collectors.toList());

        return MemberResponseDto.builder()
                .walkingResponseDto(walkingResponseDto)
                .dogs(dogProfileResponseDtos)
                .build();
    }

    @Transactional
    public Member addAdditionalInfo(MemberAdditionalInfoRequestDto additionalInfoRequestDto) {
        Member member = getMemberFromUserDetail();

        String profileUrl = null;
        if (additionalInfoRequestDto.getProfile() != null && !additionalInfoRequestDto.getProfile().isEmpty()) {
            profileUrl = fileService.uploadFile(additionalInfoRequestDto.getProfile(), "member");
        }

        member.updateAdditionalInfo(additionalInfoRequestDto, profileUrl);

        memberRepository.save(member);
        return member;
    }

    @Transactional(readOnly = true)
    protected WalkingResponseDto getWalkingResponseDto (Long memberId) {
        WalkingBasicInfo walkingBasicInfo = memberRepository.getWalkingInfo(memberId, "N");

        List<HashtagPercentageDto> topHashtags = hashtagToWalkerRepository.getTopHashtags(memberId, "N");


        return WalkingResponseDto.builder()
                .walkingBasicInfo(walkingBasicInfo)
                .topHashtags(topHashtags)
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberByMemberId(Long reviewerId) {
        return memberRepository.findByMemberIdAndDelYn(reviewerId, "N");
    }
}
