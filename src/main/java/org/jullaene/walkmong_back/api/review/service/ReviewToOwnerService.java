package org.jullaene.walkmong_back.api.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.api.review.domain.ReviewToOwner;
import org.jullaene.walkmong_back.api.review.domain.ReviewToOwnerImage;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToOwnerReqDto;
import org.jullaene.walkmong_back.api.review.repository.ReviewToOwnerImageRepository;
import org.jullaene.walkmong_back.api.review.repository.ReviewToOwnerRepository;
import org.jullaene.walkmong_back.common.file.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewToOwnerService {
    private final MemberService memberService;
    private final ApplyService applyService;
    private final FileService fileService;
    private final ReviewToOwnerRepository reviewToOwnerRepository;
    private final ReviewToOwnerImageRepository reviewToOwnerImageRepository;
    public Long registerReviewToOwner(ReviewToOwnerReqDto reviewToOwnerReqDto) {
        Member member = memberService.getMemberFromUserDetail();

        applyService.isValidWalkerByBoardIdAndMatchingStatus(member.getMemberId(), reviewToOwnerReqDto.getBoardId(), MatchingStatus.CONFIRMED);
        log.info("해당 산책의 산책자 인증 완료");

        ReviewToOwner reviewToOwner = ReviewToOwner.builder()
                .reviewToOwnerReqDto(reviewToOwnerReqDto)
                .reviewerId(member.getMemberId())
                .build();

        Long reviewToOwnerId = reviewToOwnerRepository.save(reviewToOwner).getReviewToOwnerId();
        log.info("산책자 -> 반려인 리뷰 작성 완료 : " + reviewToOwnerId);

        List<ReviewToOwnerImage> images = reviewToOwnerReqDto.getImages().stream()
                .map(image -> {
                    String imageUrl = fileService.uploadFile(image, "/review/to/walker");
                    return ReviewToOwnerImage.builder()
                            .reviewToOwnerId(reviewToOwnerId)
                            .imageUrl(imageUrl)
                            .build();
                })
                .toList();
        reviewToOwnerImageRepository.saveAll(images);
        log.info("산책자 -> 반려인 리뷰 이미지 저장 완료");

        return reviewToOwnerId;
    }
}
