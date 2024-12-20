package org.jullaene.walkmong_back.api.apply.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplyInfoDto;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @GetMapping("/{boardId}")
    public ResponseEntity<BasicResponse<ApplyInfoDto>> getApplyInfo(
            @PathVariable("boardId") Long boardId
    ){
        ApplyInfoDto applyInfoDto=applyService.getApplyInfo(boardId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(applyInfoDto));
    }

}
