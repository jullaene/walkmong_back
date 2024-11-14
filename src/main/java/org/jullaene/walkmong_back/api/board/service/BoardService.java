package org.jullaene.walkmong_back.api.board.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
}
