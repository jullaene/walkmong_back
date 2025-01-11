package org.jullaene.walkmong_back.api.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.IdentifierLoadAccess;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.chat.domain.Chat;
import org.jullaene.walkmong_back.api.chat.domain.ChatRoom;
import org.jullaene.walkmong_back.api.chat.domain.enums.MessageType;
import org.jullaene.walkmong_back.api.chat.dto.req.ChatMessageRequestDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatHistoryResponseDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatMessageResponseDto;
import org.jullaene.walkmong_back.api.chat.repository.ChatRepository;
import org.jullaene.walkmong_back.api.chat.repository.ChatRoomRepository;

import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final BoardRepository boardRepository;

    //채팅방 생성
    public Long createRoom(Long boardId) {
        //채팅방에 입장한 사람
        Long participantId = memberService.getMemberFromUserDetail().getMemberId();

        //채팅방 주인 찾기
        Long ownerId = boardRepository.findOwnerIdByBoardId(boardId);

        ChatRoom chatRoom = ChatRoom.builder().
                boardId(boardId).
                chatOwnerId(ownerId).
                participantId(participantId)
                .build();

        Long roomId = chatRoom.getRoomId();

        chatRoomRepository.save(chatRoom);

        return roomId;
    }

    /**
     채팅방에 있는 대화 내역 조회
     */
    public List<ChatHistoryResponseDto> getChatHistory(Long roomId) {
        List<Chat> chatList=chatRepository.findAllByRoomId(roomId);
        List<ChatHistoryResponseDto> chatHistoryResponseDtoList=new ArrayList<>();
        for (Chat chat:chatList){
            ChatHistoryResponseDto chatHistoryResponseDto=new ChatHistoryResponseDto(
                    chat.getMessage(),
                    chat.getSenderId(),
                    chat.getCreatedAt());
            chatHistoryResponseDtoList.add(chatHistoryResponseDto);
        }

        return chatHistoryResponseDtoList;
    }

    //메세지 저장
    public ChatMessageResponseDto saveMessage(ChatMessageRequestDto message) {
        // 메시지 보낸 사람
        Member member = memberService.getMemberFromUserDetail();

        ChatRoom chatRoom = chatRoomRepository.findByRoomIdAndDelYn(message.getRoomId(), "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_CHAT_ROOM));
        log.info("member :  " + member.getNickname());

        // 방에 접근 가능한 사람인지 확인
        if (!chatRoom.getChatOwnerId().equals(member.getMemberId()) && !chatRoom.getChatParticipantId().equals(member.getMemberId())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
        }

        // 입, 퇴장인 경우 메시지 다르게 적용
        String newMessage = message.getMessage();

        if (message.getType().equals(MessageType.ENTER) || message.getType().equals(MessageType.LEAVE)) {
            newMessage = member.getNickname() + "가 " + newMessage;
        }
        log.info("neww mesage : " + newMessage);

        Chat chat = Chat.builder()
                .senderId(member.getMemberId())
                .roomId(message.getRoomId())
                .message(newMessage)
                .type(message.getType())
                .build();

        Chat savedChat = chatRepository.save(chat);

        return ChatMessageResponseDto.builder()
                .type(message.getType())
                .roomId(message.getRoomId())
                .senderNm(member.getNickname())
                .message(newMessage)
                .createdAt(savedChat.getCreatedAt())
                .build();
    }
}
