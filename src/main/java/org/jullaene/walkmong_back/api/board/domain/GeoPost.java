package org.jullaene.walkmong_back.api.board.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.jullaene.walkmong_back.api.board.dto.req.GeoReq;
import org.jullaene.walkmong_back.api.board.dto.res.GeoRes;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoPost implements Serializable {
    private double latitude;
    private double longitude;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime savedAt;

    @Builder
    public GeoPost(GeoReq geoReq) {
        this.latitude = geoReq.getLatitude();
        this.longitude = geoReq.getLongitude();
        this.savedAt = LocalDateTime.now();
    }

    public GeoRes toGeoRes () {
        return GeoRes.builder()
                .latitude(this.latitude)
                .longitude(this.longitude)
                .lastSavedDt(this.savedAt)
                .build();
    }
}
