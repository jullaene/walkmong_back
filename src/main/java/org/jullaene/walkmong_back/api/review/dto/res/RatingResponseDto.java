package org.jullaene.walkmong_back.api.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponseDto {
    private float timePunctuality;
    private float communication;
    private float attitude;
    private float taskCompletion;
    private float photoSharing;
    private int participants; //참여자 수
}
