package org.jullaene.walkmong_back.api.dog.dto.res;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.common.enums.Gender;

@Getter
@Builder
public class DogProfileResponseDto {
    private Long dogId;
    private String dogName;
    private String dogProfile;
    private Gender dogGender;
    private Integer dogAge;
    private String breed;
    private Double weight;
    private String neuteringYn;
    private String bite;
    private String friendly;
    private String barking;
    private String rabiesYn;

    public static DogProfileResponseDto fromDog(Dog dog) {
        int currentYear = LocalDate.now().getYear();
        int dogAge = currentYear - dog.getBirthYear() + 1;

        return DogProfileResponseDto.builder()
                .dogId(dog.getDogId())
                .dogName(dog.getName())
                .dogProfile(dog.getProfile())
                .dogGender(dog.getGender())
                .dogAge(dogAge)
                .breed(dog.getBreed())
                .weight(dog.getWeight())
                .neuteringYn(dog.getNeuteringYn())
                .bite(dog.getBite())
                .friendly(dog.getFriendly())
                .barking(dog.getBarking())
                .rabiesYn(dog.getRabiesYn())
                .build();
    }
}
