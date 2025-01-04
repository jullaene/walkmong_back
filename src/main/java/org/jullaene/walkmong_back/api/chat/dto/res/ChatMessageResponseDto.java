package org.jullaene.walkmong_back.api.chat.dto.res;

import lombok.Getter;
import lombok.Setter;
import org.jullaene.walkmong_back.api.chat.dto.req.ChatMessageRequestDto;

@Getter
@Setter
public class ChatMessageResponseDto {
    Long userId;
    Long roomId;
    ChatMessageRequestDto.MessageType messageType;
}
