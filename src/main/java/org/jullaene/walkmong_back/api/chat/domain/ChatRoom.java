package org.jullaene.walkmong_back.api.chat.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.chat.dto.req.ChatRoomRequestDto;
import org.jullaene.walkmong_back.common.BaseEntity;

@NoArgsConstructor
@Getter
@Entity
@Table(name="chatroom")
@DynamicUpdate
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long roomId;

    @Comment("채팅방 주인")
    private Long chatOwnerId;

    @Comment("채팅방에 입장한 사람")
    private Long chatParticipantId;

    @Comment("게시글 아이디")
    private Long boardId;


    @Builder
    public ChatRoom(Long boardId, Long chatOwnerId,Long participantId){
       this.boardId=boardId;
       this.chatOwnerId=chatOwnerId;
       this.chatParticipantId=participantId;

    }


}
