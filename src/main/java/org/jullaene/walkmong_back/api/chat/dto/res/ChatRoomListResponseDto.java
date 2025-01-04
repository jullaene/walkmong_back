package org.jullaene.walkmong_back.api.chat.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
public class ChatRoomListResponseDto {
    private String dogName;
    private String dogProfile;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long chatTarget;
    private String lastChat;
    private LocalDateTime lastChatTime;
    private String targetName;
    private Integer notRead;
    private Long roomId;

}
