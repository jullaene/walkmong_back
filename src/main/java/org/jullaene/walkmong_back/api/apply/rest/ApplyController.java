package org.jullaene.walkmong_back.api.apply.rest;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/apply")
public class ApplyController {
    private final ApplyService applyService;
}
