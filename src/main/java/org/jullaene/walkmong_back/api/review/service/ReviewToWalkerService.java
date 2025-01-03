package org.jullaene.walkmong_back.api.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.board.service.BoardService;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.api.review.domain.HashtagToWalker;
import org.jullaene.walkmong_back.api.review.domain.ReviewToWalker;
import org.jullaene.walkmong_back.api.review.domain.ReviewToWalkerImage;
import org.jullaene.walkmong_back.api.review.dto.req.ReviewToWalkerReqDto;
import org.jullaene.walkmong_back.api.review.dto.res.RatingResponseDto;
import org.jullaene.walkmong_back.api.review.repository.HashtagToWalkerRepository;
import org.jullaene.walkmong_back.api.review.repository.ReviewToWalkerImageRepository;
import org.jullaene.walkmong_back.api.review.repository.ReviewToWalkerRepository;
import org.jullaene.walkmong_back.common.file.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewToWalkerService {
    private final MemberService memberService;
    private final BoardService boardService;
    private final FileService fileService;
    private final ReviewToWalkerRepository reviewToWalkerRepository;
    private final HashtagToWalkerRepository hashtagToWalkerRepository;
    private final ReviewToWalkerImageRepository reviewToWalkerImageRepository;

    @Transactional
    public Long registerReviewToWalker(ReviewToWalkerReqDto reviewToWalkerReqDto) {
        Member member = memberService.getMemberFromUserDetail();

        boardService.isValidOwnerByBoardIdAndDelYn(member.getMemberId(), reviewToWalkerReqDto.getBoardId(), "N");
        log.info("해당 게시글의 반려인 인증 완료");

        ReviewToWalker reviewToWalker = ReviewToWalker.builder()
                .reviewToWalkerReqDto(reviewToWalkerReqDto)
                .reviewerId(member.getMemberId())
                .build();
        Long reviewToWalkerId = reviewToWalkerRepository.save(reviewToWalker).getReviewToWalkerId();
        log.info("산책자 리뷰 저장");

        List<HashtagToWalker> hashtagToWalkers = reviewToWalkerReqDto.getHashtags().stream()
                .map(hashtag -> {
                    return HashtagToWalker.builder()
                            .reviewToWalkerId(reviewToWalkerId)
                            .reviewTargetId(member.getMemberId())
                            .hashtagWalkerNm(hashtag)
                            .build();
                })
                .toList();
        hashtagToWalkerRepository.saveAll(hashtagToWalkers);
        log.info("산책자 리뷰 해시태그 저장 완료");

        List<ReviewToWalkerImage> images = reviewToWalkerReqDto.getImages().stream()
                        .map(image -> {
                            String imageUrl = fileService.uploadFile(image, "/review/to/walker");
                            return ReviewToWalkerImage.builder()
                                    .reviewToWalkerId(reviewToWalkerId)
                                    .imageUrl(imageUrl)
                                    .build();
                        })
                                .toList();
        reviewToWalkerImageRepository.saveAll(images);
        log.info("산책자 리뷰 이미지 저장 완료");

        return reviewToWalkerId;
    }

    /**
     * 전체 사용자 평가 평균 조회
     */
    public RatingResponseDto calculateAverage(Long walkerId){
        List<ReviewToWalker> reviews=reviewToWalkerRepository.findAllByReviewTargetId(walkerId);

        float timePunctualityAvg = (float) reviews.stream()
                .mapToDouble(ReviewToWalker::getTimePunctuality)
                .average()
                .orElse(0.0);

        float communicationAvg = (float) reviews.stream()
                .mapToDouble(ReviewToWalker::getCommunication)
                .average()
                .orElse(0.0);

        float attitudeAvg = (float) reviews.stream()
                .mapToDouble(ReviewToWalker::getAttitude)
                .average()
                .orElse(0.0);

        float taskCompletionAvg = (float) reviews.stream()
                .mapToDouble(ReviewToWalker::getTaskCompletion)
                .average()
                .orElse(0.0);

        float photoSharingAvg = (float) reviews.stream()
                .mapToDouble(ReviewToWalker::getPhotoSharing)
                .average()
                .orElse(0.0);

        return new RatingResponseDto(timePunctualityAvg, communicationAvg, attitudeAvg, taskCompletionAvg, photoSharingAvg,reviews.size());

    }

}
