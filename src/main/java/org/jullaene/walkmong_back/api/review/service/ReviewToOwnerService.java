package org.jullaene.walkmong_back.api.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.api.board.domain.Board;
import org.jullaene.walkmong_back.api.board.repository.BoardRepository;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.res.MemberResponseDto;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.api.review.domain.ReviewToOwner;
import org.jullaene.walkmong_back.api.review.domain.ReviewToOwnerImage;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToOwnerReqDto;
import org.jullaene.walkmong_back.api.review.dto.res.ReviewToOwnerResponseDto;
import org.jullaene.walkmong_back.api.review.repository.ReviewToOwnerImageRepository;
import org.jullaene.walkmong_back.api.review.repository.ReviewToOwnerRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.file.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewToOwnerService {
    private final MemberService memberService;
    private final ApplyService applyService;
    private final FileService fileService;
    private final ReviewToOwnerRepository reviewToOwnerRepository;
    private final ReviewToOwnerImageRepository reviewToOwnerImageRepository;
    private final BoardRepository boardRepository;
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
                    String imageUrl = fileService.uploadFile(image, "review/to/walker");
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

    @Transactional(readOnly = true)
    public List<ReviewToOwnerResponseDto> getReviewToOwners (Long memberId, Long dogId, String ascYn) {
        if (memberId == null || memberId <= 0) {
            memberId = memberService.getMemberFromUserDetail().getMemberId();
        }

        List<ReviewToOwner> reviewToOwners;

        if (dogId != null && dogId > 0) {
            reviewToOwners = reviewToOwnerRepository.findAllByDogIdAndDelYn(dogId, "N");
        }
        else {
            reviewToOwners = reviewToOwnerRepository.findAllByReviewTargetIdAndDelYn(memberId, "N");
        }

        List<ReviewToOwnerResponseDto> reviews = new ArrayList<>(reviewToOwners.stream()
                .map(reviewToOwner -> {
                    Board board = boardRepository.findByBoardIdAndDelYn(reviewToOwner.getBoardId(), "N")
                            .orElseThrow(() -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.INVALID_BOARD));

                    Optional<Member> member = memberService.getMemberByMemberId(reviewToOwner.getReviewerId());
                    String reviewerNm = member.isPresent() ? member.get().getNickname() : "탈퇴한 사용자";

                    List<ReviewToOwnerImage> reviewToOwnerImages = reviewToOwnerImageRepository.findAllByReviewToOwnerId(reviewToOwner.getReviewerId());
                    List<String> images = reviewToOwnerImages.stream()
                            .map(ReviewToOwnerImage::getImageUrl)
                            .toList();

                    return reviewToOwner.toReviewToOwnerResponseDto(board.getStartTime(), reviewerNm, images);
                })
                .toList());

        reviews.sort(
                "Y".equalsIgnoreCase(ascYn)
                        ? Comparator.comparing(ReviewToOwnerResponseDto::getWalkingDay).reversed()
                        : Comparator.comparing(ReviewToOwnerResponseDto::getWalkingDay)
        );

        return reviews;
    }
}
