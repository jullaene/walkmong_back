package org.jullaene.walkmong_back.api.post.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
}
