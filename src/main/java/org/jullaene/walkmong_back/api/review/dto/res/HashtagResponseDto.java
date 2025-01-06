package org.jullaene.walkmong_back.api.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.api.review.domain.enums.HashtagWalkerNm;

@Getter
@AllArgsConstructor
public class HashtagResponseDto {
    private HashtagWalkerNm hashtag;
    private Long count;
}
