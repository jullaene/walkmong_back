package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagResponseDto;
import org.jullaene.walkmong_back.api.review.dto.res.RatingResponseDto;

import java.util.List;

/**
 * 지원자의 정보와 산책 후기를 반환한다
 */
@Getter
@Builder
public class ApplicationFormResponseDto {
    private BoardPreviewResponseDto boardDto;
    private ApplicantInfoResponseDto applicantDto;
    private ApplyInfoResponseDto applyDto;
    private RatingResponseDto ratingDto;
    private List<HashtagResponseDto> hashtagDto;
}
