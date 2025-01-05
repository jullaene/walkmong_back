package org.jullaene.walkmong_back.api.member.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class WalkExperienceReq {
    private final String dogOwnershipYn;
    private final String dogWalkingExperienceYn;
    private final String availabilityWithSize;
    private final String introduction;
}
