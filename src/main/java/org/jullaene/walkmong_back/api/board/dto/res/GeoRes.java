package org.jullaene.walkmong_back.api.board.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GeoRes {
    private double latitude;
    private double longitude;
    private LocalDateTime lastSavedDt;

    @Builder
    public GeoRes(double latitude, double longitude, LocalDateTime lastSavedDt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastSavedDt = lastSavedDt;
    }
}
