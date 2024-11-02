package org.jullaene.walkmong_back.api.member.domain.enums;

public enum DistanceRange {
    SMALL(0.5),
    MEDIUM(1.0),
    BIG(1.5),
    ;

    private final Double range;

    DistanceRange(Double range) {
        this.range = range;
    }

    public Double getRange() {
        return range;
    }
}
