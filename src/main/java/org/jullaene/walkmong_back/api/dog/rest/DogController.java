package org.jullaene.walkmong_back.api.dog.rest;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.dto.req.DogProfileReqDto;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.api.dog.service.DogService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<BasicResponse<Long>> registerDogProfile(@Valid @RequestBody DogProfileReqDto dogProfileReqDto) {
            return ResponseEntity.ok(BasicResponse.ofSuccess(dogService.registerDogProfile(dogProfileReqDto)));
    }

    @GetMapping("/list")
    public ResponseEntity<BasicResponse<List<DogProfileResponseDto>>> getDogProfiles() {
        List<DogProfileResponseDto> dogProfileList = dogService.getDogProfileList();
        return ResponseEntity.ok(BasicResponse.ofSuccess(dogProfileList));
    }
}
