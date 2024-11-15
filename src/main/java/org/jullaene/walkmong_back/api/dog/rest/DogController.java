package org.jullaene.walkmong_back.api.dog.rest;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.api.dog.service.DogService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dog")
public class DogController {
    private final DogService dogService;

    @GetMapping("/profile/{dogId}")
    public ResponseEntity<BasicResponse<DogProfileResponseDto>> getDogProfile(@PathVariable Long dogId) {
        DogProfileResponseDto dogProfile = dogService.getDogProfile(dogId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(dogProfile));
    }
}
