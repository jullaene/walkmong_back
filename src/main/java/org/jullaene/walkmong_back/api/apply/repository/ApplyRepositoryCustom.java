package org.jullaene.walkmong_back.api.apply.repository;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplyInfoDto;
import java.util.Optional;

public interface ApplyRepositoryCustom {
    Optional<ApplyInfoDto> getApplyInfoResponse(Long boardId, Long memberId, String delYn);
}
