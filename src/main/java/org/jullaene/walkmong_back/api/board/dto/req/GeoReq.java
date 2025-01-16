package org.jullaene.walkmong_back.api.board.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.api.board.domain.GeoPost;

@Getter
@AllArgsConstructor
public class GeoReq {
    private double latitude;
    private double longitude;

    public GeoPost toEntity() {
        return GeoPost.builder()
                .geoReq(this)
                .build();
    }
}
