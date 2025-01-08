package org.jullaene.walkmong_back.api.review.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToWalkerReqDto;
import org.jullaene.walkmong_back.api.review.dto.res.ReviewToWalkerRes;
import org.jullaene.walkmong_back.api.review.service.ReviewToWalkerService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review/to/walker")
public class ReviewToWalkerController {

    private final ReviewToWalkerService reviewToWalkerService;


    @PostMapping("/register")
    public ResponseEntity<BasicResponse<Long>> registerReviewToWalker (@Valid @ModelAttribute ReviewToWalkerReqDto reviewToWalkerReqDto) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(reviewToWalkerService.registerReviewToWalker(reviewToWalkerReqDto)));
    }

    @Operation(summary = "산책 후기 리스트 조회 (반려인 -> 산책자)", description = "반려인이 산책자에 대해 작성한 산책 후기 리스트를 조회하는 API입니다.")
    @GetMapping("/list")
    public ResponseEntity<BasicResponse<List<ReviewToWalkerRes>>> getReviewToWalkerList () {
        return ResponseEntity.ok(BasicResponse.ofSuccess(reviewToWalkerService.getReviewToWalkerList()));
    }
}
