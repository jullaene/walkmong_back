package org.jullaene.walkmong_back.api.board.dto.res;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardPreviewResponseDto {
    private String dogName;
    private String dogProfile;
    private Gender dogGender;
    private String dongAddress;
    private String content;
    private String startTime;
    private String endTime;
}
