package org.jullaene.walkmong_back.api.chat.dto.req;

import lombok.*;
import org.jullaene.walkmong_back.api.chat.domain.enums.MessageType;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageRequestDto {
    private String message; //메세지
    private Long roomId;
    private MessageType type;
}
