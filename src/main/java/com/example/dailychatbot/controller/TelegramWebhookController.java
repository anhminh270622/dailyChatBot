package com.example.dailychatbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramWebhookController {
    @PostMapping("/webhook")
    public ResponseEntity<String> onUpdateReceived(@RequestBody String updateJson) {
        System.out.println("Received update: " + updateJson);
        // Ở đây bạn có thể parse JSON để lấy tin nhắn và chatId xử lý tiếp
        return ResponseEntity.ok("OK");
    }
}
