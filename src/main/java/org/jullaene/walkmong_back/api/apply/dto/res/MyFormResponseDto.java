package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyFormResponseDto {
    private BoardPreviewResponseDto previewResponseDto;
    private ApplyInfoResponseDto applyInfoResponseDto;
}
