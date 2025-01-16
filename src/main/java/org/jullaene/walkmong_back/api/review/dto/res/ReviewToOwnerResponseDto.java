package org.jullaene.walkmong_back.api.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.review.domain.enums.Activity;
import org.jullaene.walkmong_back.api.review.domain.enums.Aggressiveness;
import org.jullaene.walkmong_back.api.review.domain.enums.Sociality;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewToOwnerResponseDto {
    private final String reviewer;
    private final String reviewerProfile;
    private final LocalDateTime walkingDay;
    private final Sociality sociality;
    private final Activity activity;
    private final Aggressiveness aggressiveness;
    private final String content;
    private final List<String> images;

    @Builder
    public ReviewToOwnerResponseDto(String reviewer, String reviewerProfile, LocalDateTime walkingDay, Sociality sociality, Activity activity, Aggressiveness aggressiveness, String content, List<String> images) {
        this.reviewer = reviewer;
        this.reviewerProfile = reviewerProfile;
        this.walkingDay = walkingDay;
        this.sociality = sociality;
        this.activity = activity;
        this.aggressiveness = aggressiveness;
        this.content = content;
        this.images = images;
    }
}
