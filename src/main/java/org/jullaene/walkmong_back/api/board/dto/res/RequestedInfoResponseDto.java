package org.jullaene.walkmong_back.api.board.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.dto.res.RecordResponseDto;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDateTime;

//의뢰한 산책
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestedInfoResponseDto extends RecordResponseDto {
    private String dogName;
    private Gender dogGender;
    private String dogProfile;
    private String dongAddress;
    private String addressDetail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String walkerNickname;
    private String walkerProfile;
}
