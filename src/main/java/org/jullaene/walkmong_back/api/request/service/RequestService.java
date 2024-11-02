package org.jullaene.walkmong_back.api.request.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.request.repository.RequestRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
}
