package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;

import lombok.Getter;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApplyInfoDto {
     private String dogName;
     private Gender dogGender;
     private String breed;
     private DogSize dogSize;
     private String ownerName;
     private String memberProfile;
     private Gender memberGender;
     private String dongAddress;
     private String addressDetail;
     private String muzzleYn;
     private String poopBagYn;
     private String preMeetingYn;
     private String memoToOwner;
     private LocalDateTime startTime;
     private LocalDateTime endTime;
}
