package org.jullaene.walkmong_back.api.review.dto.req;

import lombok.Getter;
import org.jullaene.walkmong_back.api.review.domain.enums.HashtagWalkerNm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ReviewToWalkerReqDto {
    private final Long walkerId;
    private final Long boardId;
    private final Float timePunctuality;
    private final Float communication;
    private final Float attitude;
    private final Float taskCompletion;
    private final Float photoSharing;
    private final String content;
    private final List<HashtagWalkerNm> hashtags;
    private final List<MultipartFile> images;

    public ReviewToWalkerReqDto(Long walkerId, Long boardId, Float timePunctuality, Float communication, Float attitude, Float taskCompletion, Float photoSharing, String content, List<HashtagWalkerNm> hashtags, List<MultipartFile> images) {
        this.walkerId = walkerId;
        this.boardId = boardId;
        this.timePunctuality = timePunctuality;
        this.communication = communication;
        this.attitude = attitude;
        this.taskCompletion = taskCompletion;
        this.photoSharing = photoSharing;
        this.content = content;
        this.hashtags = hashtags;
        this.images = images;
    }
}
