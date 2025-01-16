package org.jullaene.walkmong_back.api.dog.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.api.dog.dto.req.DogProfileReqDto;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.file.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DogService {
    private final DogRepository dogRepository;
    private final MemberService memberService;
    private final FileService fileService;

    public DogProfileResponseDto getDogProfile(Long dogId) {
        Dog dog = dogRepository.findByDogIdAndDelYn(dogId, "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.DOG_NOT_FOUND));

        return dog.toDogProfileResponseDto();
    }

    public Long registerDogProfile(DogProfileReqDto dogProfileReqDto) {

        Member member = memberService.getMemberFromUserDetail();

        //이미 등록된 강아지 프로필이라면 예외처리
        if (dogRepository.existsByNameAndMemberIdAndDelYn(dogProfileReqDto.getName(), member.getMemberId(), "N")){
            throw new CustomException(HttpStatus.FORBIDDEN,ErrorType.CANNOT_DUPLICATED_DOG_PROFILE);
        }

        String imageUrl = fileService.uploadFile(dogProfileReqDto.getProfile(), "dog");

        Dog dog=Dog.builder()
                .memberId(member.getMemberId())
                .dogProfileReqDto(dogProfileReqDto)
                .profileUrl(imageUrl)
                .build();

        return dogRepository.save(dog).getDogId();
    }

    public DogProfileResponseDto updateDogProfile(Long dogId, DogProfileReqDto dogProfileReqDto) {

        Member member = memberService.getMemberFromUserDetail();

        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.DOG_NOT_FOUND));

        // 강아지 주인인지 확인
        boolean isDogOwnedByMember = member.getMemberId().equals(dog.getMemberId());

        if (!isDogOwnedByMember) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorType.ACCESS_DENIED);
        }

        String imageUrl = fileService.uploadFile(dogProfileReqDto.getProfile(), "dog");
        dog.updateProfile(dogProfileReqDto, imageUrl);
        Dog updatedDog = dogRepository.save(dog);

        return updatedDog.toDogProfileResponseDto();
    }

    public List<DogProfileResponseDto> getDogProfileList() {
        Member member = memberService.getMemberFromUserDetail();
        List<Dog> dogs = dogRepository.findByMemberIdAndDelYn(member.getMemberId(), "N");

        return dogs.stream()
                .map(Dog::toDogProfileResponseDto)
                .collect(Collectors.toList());
    }
}
