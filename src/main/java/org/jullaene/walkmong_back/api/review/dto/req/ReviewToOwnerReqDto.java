package org.jullaene.walkmong_back.api.review.dto.req;

import lombok.Getter;
import org.jullaene.walkmong_back.api.review.domain.enums.Activity;
import org.jullaene.walkmong_back.api.review.domain.enums.Aggressiveness;
import org.jullaene.walkmong_back.api.review.domain.enums.Sociality;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ReviewToOwnerReqDto {
    private Long ownerId;
    private Long boardId;
    private String goodYn;
    private Sociality sociality;
    private Activity activity;
    private Aggressiveness aggressiveness;
    private String disappointment;
    private String content;
    private List<MultipartFile> images;
}
