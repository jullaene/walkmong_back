package org.jullaene.walkmong_back.api.chat.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatHistoryResponseDto {
    Long chatId;
    String message;
    Long roomId;
    Long senderId;
}
