package org.jullaene.walkmong_back.api.review.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToOwnerReqDto;
import org.jullaene.walkmong_back.api.review.service.ReviewToOwnerService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review/to/owner")
public class ReviewToOwnerController {
    private final ReviewToOwnerService reviewToOwnerService;


    @PostMapping("/register")
    public ResponseEntity<BasicResponse<Long>> registerReviewToOwner(@Valid @ModelAttribute ReviewToOwnerReqDto reviewToOwnerReqDto) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(reviewToOwnerService.registerReviewToOwner(reviewToOwnerReqDto)));
    }
}
