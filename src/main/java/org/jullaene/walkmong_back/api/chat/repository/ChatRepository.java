package org.jullaene.walkmong_back.api.chat.repository;

import org.jullaene.walkmong_back.api.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
   List<Chat> findAllByRoomId(Long roomId);
}
