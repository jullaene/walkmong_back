package org.jullaene.walkmong_back.api.member.domain.enums;

public enum DistanceRange {
    SMALL(500.0),
    MEDIUM(1000.0),
    BIG(1500.0),
    ;

    private final Double range;

    DistanceRange(Double range) {
        this.range = range;
    }

    public Double getRange() {
        return range;
    }
}
