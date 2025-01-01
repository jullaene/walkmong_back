package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 반려인이 산책 지원자 리스트를 조회한다
 */
@Getter
public class ApplicantWithBoardResponseDto {
    private List<ApplicantListResponseDto> applicantDto;
    private BoardPreviewResponseDto boardDto;


    public ApplicantWithBoardResponseDto(List<ApplicantListResponseDto> applicantDto, BoardPreviewResponseDto boardDto){
        this.applicantDto=applicantDto;
        this.boardDto=boardDto;
    }

}
