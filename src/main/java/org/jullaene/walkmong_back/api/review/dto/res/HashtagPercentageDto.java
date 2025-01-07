package org.jullaene.walkmong_back.api.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.api.review.domain.enums.HashtagWalkerNm;

@Getter
@AllArgsConstructor
public class HashtagPercentageDto {
    private final HashtagWalkerNm hashtagNm;
    private final Integer goodPercent;
}
