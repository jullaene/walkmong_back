package org.jullaene.walkmong_back.api.apply.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.domain.Apply;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;

    @Transactional
    public Long saveApply(ApplyRequestDto applyRequestDto){
        Apply apply=Apply.builder()
                .dongAddress(applyRequestDto.getDongAddress())
                .roadAddress(applyRequestDto.getRoadAddress())
                .latitude(applyRequestDto.getLatitude())
                .longitude(applyRequestDto.getLongitude())
                .addressDetail(applyRequestDto.getAddressDetail())
                .addressMemo(applyRequestDto.getAddressMemo())
                .poopBagYn(applyRequestDto.getPoopBagYn())
                .muzzleYn(applyRequestDto.getMuzzleYn())
                .dogCollarYn(applyRequestDto.getDogCollarYn())
                .preMeetingYn(applyRequestDto.getPreMeetingYn())
                .memoToOwner(applyRequestDto.getMemoToOwner())
                .build();

        return  applyRepository.save(apply).getApplyId();
    }
}
