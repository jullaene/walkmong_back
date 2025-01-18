package org.jullaene.walkmong_back.api.chat.dto.res;

import lombok.*;
import org.jullaene.walkmong_back.api.board.domain.Board;
import org.jullaene.walkmong_back.api.chat.domain.Chat;
import org.jullaene.walkmong_back.api.chat.domain.ChatRoom;
import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.common.enums.TabStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Setter
public class ChatRoomListResponseDto {
    private TabStatus tabStatus;
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
    private Long boardId;

    @Builder
    public ChatRoomListResponseDto(Dog dog,
                                   Board board,
                                   Member target,
                                   Chat chat,
                                   ChatRoom chatRoom,
                                   TabStatus tabStatus
    ) {
        this.tabStatus = tabStatus;
        this.dogName = dog.getName();
        this.dogProfile = dog.getProfile();
        this.startTime = board.getStartTime();
        this.endTime = board.getEndTime();
        this.chatTarget = target.getMemberId();
        this.targetName = target.getNickname();
        this.notRead = 0;
        this.roomId = chatRoom.getRoomId();
        this.boardId = board.getBoardId();

        if (chat != null) {
            this.lastChat = chat.getMessage();
            this.lastChatTime = chat.getCreatedAt();
        }
    }
}
