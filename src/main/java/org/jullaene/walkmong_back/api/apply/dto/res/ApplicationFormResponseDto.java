package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.review.dto.res.RatingResponseDto;

/**
 * 지원자의 정보와 산책 후기를 반환한다
 */
@Getter
@Builder
public class ApplicationFormResponseDto {
    BoardPreviewResponseDto boardDto;
    WalkerInfoResponseDto walkerDto;
    RatingResponseDto ratingDto;
}
