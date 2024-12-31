package org.jullaene.walkmong_back.api.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.chat.domain.Chat;
import org.jullaene.walkmong_back.api.chat.domain.ChatRoom;
import org.jullaene.walkmong_back.api.chat.dto.ChatMessageRequestDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatHistoryResponseDto;
import org.jullaene.walkmong_back.api.chat.repository.ChatRepository;
import org.jullaene.walkmong_back.api.chat.repository.ChatRoomRepository;

import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.springframework.stereotype.Service;

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

    //메세지 저장
    public void saveMessage(ChatMessageRequestDto chatMessageRequestDto, Long memberId) {
        Chat chat=Chat.builder()
                .senderId(memberId)
                .roomId(chatMessageRequestDto.getRoomNumber())
                .message(chatMessageRequestDto.getMsg())
                .build();

        log.info("메세지 저장");
        chatRepository.save(chat);
    }

    //채팅방에 있는 대화 내역 조회
    public List<ChatHistoryResponseDto> getChatHistory(Long roomId) {
        List<Chat> chatList=chatRepository.findAllByRoomId(roomId);
        List<ChatHistoryResponseDto> chatHistoryResponseDtoList=new ArrayList<>();
        for (Chat chat:chatList){
            ChatHistoryResponseDto chatHistoryResponseDto=new ChatHistoryResponseDto(
                    chat.getMessage(),
                    chat.getRoomId(),
                    chat.getSenderId(),
                    chat.getCreatedAt());
            chatHistoryResponseDtoList.add(chatHistoryResponseDto);
        }

        return chatHistoryResponseDtoList;
    }
}
