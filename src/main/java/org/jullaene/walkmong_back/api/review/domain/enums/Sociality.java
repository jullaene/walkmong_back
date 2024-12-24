package org.jullaene.walkmong_back.api.review.domain.enums;

public enum Sociality {
    PEOPLE_FRIENDLY("사람 좋아함"),
    DOG_FRIENDLY("강아지 좋아함"),
    SHY("낯가림 있음"),
    PLAYFUL("애교 많음"),
    GUARDED("경계심 심함");

    private final String name;

    Sociality(String name) {
        this.name = name;
    }
}
