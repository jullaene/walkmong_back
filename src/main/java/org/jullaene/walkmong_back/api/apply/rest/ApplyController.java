package org.jullaene.walkmong_back.api.apply.rest;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/apply")
public class ApplyController {
    private final ApplyService applyService;

    @PostMapping("/{boardId}")
    public ResponseEntity<BasicResponse<Long>> saveApply(@RequestBody ApplyRequestDto applyRequestDto){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(applyService.saveApply(applyRequestDto)));
    }
}
