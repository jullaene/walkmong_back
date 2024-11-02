package org.jullaene.walkmong_back.api.dog.rest;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.service.DogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dog")
public class DogController {
    private final DogService dogService;
}
