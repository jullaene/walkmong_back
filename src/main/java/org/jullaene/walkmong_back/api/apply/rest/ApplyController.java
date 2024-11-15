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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/walking/apply")
public class ApplyController {
    private final ApplyService applyService;

    @PostMapping("/{boardId}")
    public ResponseEntity<BasicResponse<Long>> saveApply(
            @PathVariable("boardId") Long boardId,
            @RequestBody ApplyRequestDto applyRequestDto){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(applyService.saveApply(boardId, applyRequestDto)));
    }
}
