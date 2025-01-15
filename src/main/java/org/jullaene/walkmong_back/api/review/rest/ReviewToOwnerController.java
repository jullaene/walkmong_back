package org.jullaene.walkmong_back.api.review.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToOwnerReqDto;
import org.jullaene.walkmong_back.api.review.dto.res.ReviewToOwnerResponseDto;
import org.jullaene.walkmong_back.api.review.dto.res.ReviewToWalkerRes;
import org.jullaene.walkmong_back.api.review.service.ReviewToOwnerService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review/to/owner")
public class ReviewToOwnerController {
    private final ReviewToOwnerService reviewToOwnerService;


    @PostMapping("/register")
    public ResponseEntity<BasicResponse<Long>> registerReviewToOwner(@Valid @ModelAttribute ReviewToOwnerReqDto reviewToOwnerReqDto) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(reviewToOwnerService.registerReviewToOwner(reviewToOwnerReqDto)));
    }

    @Operation(summary = "산책 후기 리스트 조회 (산책자 -> 반려인)", description = "산책자가 반려인에 대해 작성한 산책 후기 리스트를 조회하는 API입니다.")
    @GetMapping("/list")
    public ResponseEntity<BasicResponse<List<ReviewToOwnerResponseDto>>> getReviewToOwnerList (
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "dogId", required = false) Long dogId,
            @RequestParam(value = "ascYn", defaultValue = "N") String ascYn
    ) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(reviewToOwnerService.getReviewToOwners(memberId, dogId, ascYn)));
    }
}
