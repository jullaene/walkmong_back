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
import org.jullaene.walkmong_back.common.file.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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
        if (dogRepository.existsByNameAndDelYn(dogProfileReqDto.getName(),"N")){
            throw new CustomException(HttpStatus.FORBIDDEN,ErrorType.CANNOT_DUPLICATED_DOG_PROFILE);
        }

        String imageUrl = fileService.uploadFile(dogProfileReqDto.getProfile(), "/dog");

        Dog dog=Dog.builder()
                .memberId(member.getMemberId())
                .dogProfileReqDto(dogProfileReqDto)
                .profileUrl(imageUrl)
                .build();

        return dogRepository.save(dog).getDogId();
    }

    public DogProfileResponseDto updateDogProfile(Long dogId, DogProfileReqDto dogProfileReqDto) {

        Member member = memberService.getMemberFromUserDetail();

        List<Dog> dogList = dogRepository.findByMemberIdAndDelYn(member.getMemberId(), "N");

        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.DOG_NOT_FOUND));

        boolean isDogOwnedByMember = dogList.stream()
                .anyMatch(memberDog -> memberDog.getDogId().equals(dogId));

        if (!isDogOwnedByMember) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorType.ACCESS_DENIED);
        }

        String imageUrl = fileService.uploadFile(dogProfileReqDto.getProfile(), "/dog");
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
