package org.jullaene.walkmong_back.api.chat.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/chat")
@RequiredArgsConstructor
public class ChatTestController {
    @Value("${websocket.endpoint}")
    private String websocketEndpoint;

    @GetMapping("/login")
    public String loginPage() {
        return "chat/login";
    }

    @GetMapping("")
    public String chatTest(Model model) {
        model.addAttribute("websocketEndpoint", websocketEndpoint);
        return "chat/test";
    }

}