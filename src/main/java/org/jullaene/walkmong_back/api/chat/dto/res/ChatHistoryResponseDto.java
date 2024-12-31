package org.jullaene.walkmong_back.api.chat.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatHistoryResponseDto {
    String message;
    Long roomId;
    Long senderId;
    LocalDateTime createdAt;
}
