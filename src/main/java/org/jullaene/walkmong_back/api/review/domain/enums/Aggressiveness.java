package org.jullaene.walkmong_back.api.review.domain.enums;

public enum Aggressiveness {
    DOCILE("온순"),
    OCCASIONAL_BARKING("가끔 짖음"),
    FREQUENT_BARKING("자주 짖음"),
    BITING("물음"),
    NIPPING("입질");

    private final String name;

    Aggressiveness(String name) {
        this.name = name;
    }
}
