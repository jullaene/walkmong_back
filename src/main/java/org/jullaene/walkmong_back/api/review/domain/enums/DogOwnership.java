package org.jullaene.walkmong_back.api.review.domain.enums;

public enum DogOwnership {
    NONE("없음"),
    LESS_THAN_3("3년 미만"),
    MORE_THAN_3("3년 이상"),
    MORE_THAN_5("5년 이상"),
    MORE_THAN_10("10년 이상");

    private final String name;

    DogOwnership(String name) {
        this.name = name;
    }
}
