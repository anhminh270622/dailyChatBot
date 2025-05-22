package com.example.dailychatbot.controller;

import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.response.ExpenseResponse;
import com.example.dailychatbot.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/webhook")
@RestController
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;

    @PostMapping()
    public ResponseEntity<ExpenseResponse> onUpdateReceived(@RequestBody TelegramMessage telegramMessage) {
        ExpenseResponse response = expenseService.saveFromTelegram(telegramMessage);
        System.out.println("telegramMessage: " + telegramMessage);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<ExpenseResponse>> getAll() {
        List<ExpenseResponse> response = expenseService.getAllExpenses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable String id) {
        ExpenseResponse response = expenseService.getExpenseById(id);
        return ResponseEntity.ok(response);
    }

}
