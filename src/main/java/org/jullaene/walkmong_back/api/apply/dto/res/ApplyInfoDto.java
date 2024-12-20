package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ApplyInfoDto {
    private final String dogName;
    private final Gender dogGender;
    private final String dogBreed;
    private final DogSize dogSize;
    private final String memberName;
    private final String memberProfile;
    private final Gender memberGender;
    private final String dongAddress;
    private final LocalDateTime boardStartTime;
    private final LocalDateTime boardEndTime;
    private final String addressDetail;
    private final String muzzleYn;
    private final String poopBagYn;
    private final String preMeetingYn;
    private final String memoToOwner;
}
