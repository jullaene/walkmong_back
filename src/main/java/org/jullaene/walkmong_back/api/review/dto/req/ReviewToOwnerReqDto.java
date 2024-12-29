package org.jullaene.walkmong_back.api.review.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.review.domain.enums.Activity;
import org.jullaene.walkmong_back.api.review.domain.enums.Aggressiveness;
import org.jullaene.walkmong_back.api.review.domain.enums.Sociality;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ReviewToOwnerReqDto {
    private final Long ownerId;
    private final Long boardId;
    private final String goodYn;
    private final Sociality sociality;
    private final Activity activity;
    private final Aggressiveness aggressiveness;
    private final String disappointment;
    private final String content;
    private final List<MultipartFile> images;

    @Builder
    public ReviewToOwnerReqDto(Long ownerId, Long boardId, String goodYn, Sociality sociality, Activity activity, Aggressiveness aggressiveness, String disappointment, String content, List<MultipartFile> images) {
        this.ownerId = ownerId;
        this.boardId = boardId;
        this.goodYn = goodYn;
        this.sociality = sociality;
        this.activity = activity;
        this.aggressiveness = aggressiveness;
        this.disappointment = disappointment;
        this.content = content;
        this.images = images;
    }
}
