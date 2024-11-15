package org.jullaene.walkmong_back.api.dog.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DogService {
    private final DogRepository dogRepository;

    public DogProfileResponseDto getDogProfile(Long dogId) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.DOG_NOT_FOUND));

        return DogProfileResponseDto.fromDog(dog);
    }
}
