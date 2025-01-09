package org.jullaene.walkmong_back.api.chat.repository;

import org.jullaene.walkmong_back.api.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findByRoomIdAndDelYn(Long chatRoomId, String delYn);
}
