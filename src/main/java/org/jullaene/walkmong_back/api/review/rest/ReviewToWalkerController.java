package org.jullaene.walkmong_back.api.review.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToWalkerReqDto;
import org.jullaene.walkmong_back.api.review.service.ReviewToWalkerService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review/to/walker")
public class ReviewToWalkerController {

    private final ReviewToWalkerService reviewToWalkerService;


    @PostMapping("/register")
    public ResponseEntity<BasicResponse<Long>> registerReviewToOwner(@Valid @ModelAttribute ReviewToWalkerReqDto reviewToWalkerReqDto) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(reviewToWalkerService.registerReviewToWalker(reviewToWalkerReqDto)));
    }
}
