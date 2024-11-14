package org.jullaene.walkmong_back.api.apply.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
}
