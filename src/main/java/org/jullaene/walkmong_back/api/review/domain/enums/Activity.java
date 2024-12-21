package org.jullaene.walkmong_back.api.review.domain.enums;

public enum Activity {
    RUNNING_CONSTANTLY("계속 뜀"),
    RUNNING_OCCASIONALLY("가끔 뜀"),
    WALKING_FAST("빠르게 걸음"),
    WALKING_SLOWLY("천천히 걸음"),
    FREQUENTLY_STOPPING("자주 멈춤");

    private final String name;

    Activity(String name) {
        this.name = name;
    }
}
