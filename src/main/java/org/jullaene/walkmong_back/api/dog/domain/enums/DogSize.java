package org.jullaene.walkmong_back.api.dog.domain.enums;

public enum DogSize {
    SMALL("소형견"),
    MEDIUM("중형견"),
    BIG("대형견");

    private final String koreanName;

    DogSize(String koreanName) {
        this.koreanName = koreanName;
    }

    @Override
    public String toString() {
        return koreanName;
    }
}
