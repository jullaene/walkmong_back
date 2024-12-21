package org.jullaene.walkmong_back.api.dog.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.api.dog.dto.req.DogProfileReqDto;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DogService {
    private final DogRepository dogRepository;
    private final MemberService memberService;

    public DogProfileResponseDto getDogProfile(Long dogId) {
        Dog dog = dogRepository.findByDogIdAndDelYn(dogId, "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.DOG_NOT_FOUND));

        return dog.toDogProfileResponseDto();
    }

    public Long registerDogProfile(DogProfileReqDto dogProfileReqDto) {

        //이미 등록된 강아지 프로필이라면 예외처리
        if (dogRepository.existsByNameAndDelYn(dogProfileReqDto.getName(),"N")){
            throw new CustomException(HttpStatus.FORBIDDEN,ErrorType.CANNOT_DUPLICATED_DOG_PROFILE);
        }

        Dog dog=Dog.builder()
                .name(dogProfileReqDto.getName())
                .memberId(memberService.getMemberFromUserDetail().getMemberId())
                .dogSize(dogProfileReqDto.getDogSize())
                .profile(dogProfileReqDto.getProfile())
                .gender(dogProfileReqDto.getGender())
                .birthYear(dogProfileReqDto.getBirthYear())
                .breed(dogProfileReqDto.getBreed())
                .weight(dogProfileReqDto.getWeight())
                .neuteringYn(dogProfileReqDto.getNeuteringYn())
                .bite(dogProfileReqDto.getBite())
                .friendly(dogProfileReqDto.getFriendly())
                .barking(dogProfileReqDto.getBarking())
                .rabiesYn(dogProfileReqDto.getRabiesYn())
                .adultYn(dogProfileReqDto.getAdultYn())
                .build();

        return dogRepository.save(dog).getDogId();
    }

    public List<DogProfileResponseDto> getDogProfileList() {
        Member member = memberService.getMemberFromUserDetail();
        List<Dog> dogs = dogRepository.findByMemberIdAndDelYn(member.getMemberId(), "N");

        return dogs.stream()
                .map(Dog::toDogProfileResponseDto)
                .collect(Collectors.toList());
    }

}
