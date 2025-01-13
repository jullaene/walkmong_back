package org.jullaene.walkmong_back.api.board.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardRequestDto {
    private Long dogId;

    private Long addressId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime endTime;

    private String locationNegotiationYn;

    private String preMeetAvailableYn;

    private String walkRequest;

    private String walkNote;

    private String additionalRequest;
}

