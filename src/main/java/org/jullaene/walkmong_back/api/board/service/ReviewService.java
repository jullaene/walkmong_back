package org.jullaene.walkmong_back.api.board.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
}
