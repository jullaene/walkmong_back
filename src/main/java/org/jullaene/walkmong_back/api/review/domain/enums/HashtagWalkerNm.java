package org.jullaene.walkmong_back.api.review.domain.enums;

public enum HashtagWalkerNm {
    LIKED_BY_DOG("반려견이 좋아해요"),
    POLITE("매너가 좋아요"),
    DETAIL_ORIENTED("꼼꼼해요"),
    GOOD_SCHEDULE_MANAGEMENT("일정 조정을 잘 해줘요"),
    RESPONSIBLE_WALKING("산책을 성실히 해줘요"),
    GOOD_WITH_DOGS("반려견을 잘 다뤄요"),
    FAST_RESPONSE("답장이 빨라요"),
    FOLLOWS_REQUESTS("요청 사항을 잘 들어줘요"),
    RELIABLE("믿고 맡길 수 있어요"),
    SAFE_WALKING("안전한 산책을 제공해요"),
    PROFESSIONAL("전문적으로 느껴져요");

    private final String name;

    HashtagWalkerNm(String name) {
        this.name = name;
    }
}
