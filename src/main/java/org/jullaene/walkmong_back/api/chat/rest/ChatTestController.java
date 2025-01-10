package org.jullaene.walkmong_back.api.chat.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/chat")
@RequiredArgsConstructor
public class ChatTestController {

    @GetMapping("/login")
    public String loginPage() {
        return "chat/login";  // templates/chat/login.html을 찾습니다
    }

    @GetMapping("")
    public String chatTest(Model model) {
        return "chat/test";
    }

}