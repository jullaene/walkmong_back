package org.jullaene.walkmong_back.api.dog.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.repository.DogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DogService {
    private final DogRepository dogRepository;
}
