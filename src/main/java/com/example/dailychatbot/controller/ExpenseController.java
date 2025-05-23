package com.example.dailychatbot.controller;

import com.example.dailychatbot.dto.request.ParsedTransaction;
import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.request.UpdateMessage;
import com.example.dailychatbot.dto.response.ExpenseResponse;
import com.example.dailychatbot.service.ExpenseService;
import com.example.dailychatbot.utils.BotMessages;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@RequestBody UpdateMessage update) {
        TelegramMessage message = update.getMessage();
        String messageText = message.getText().trim();
        Long chatId = message.getChat().getId();

        if (messageText.equalsIgnoreCase("/thongke")) {
            expenseService.sendStatistical(chatId);
            return ResponseEntity.ok().build(); // không cần trả response chi tiêu
        }

        if (messageText.equalsIgnoreCase("expense")) {
            // Có thể xử lý riêng nếu cần
            return ResponseEntity.ok().build();
        }

        if (messageText.equalsIgnoreCase("/start")) {
            expenseService.getWelcomeMessage(chatId);
            return ResponseEntity.ok().build();
        }
        // Xử lý mặc định: tin nhắn nhập thu/chi
        ExpenseResponse response;
        String reply;
        String replyGuide = BotMessages.TRANSACTION_GUIDE;
        try {
            response = expenseService.saveFromTelegram(message);
            ParsedTransaction parsed = expenseService.parseTransactionMessage(messageText);

            if (parsed.isExpense()) {
                reply = String.format("💰 Đã ghi nhận chi %s", parsed.getFormattedText());
            } else if (parsed.isIncome()) {
                reply = String.format("✅ Đã ghi nhận thu %s", parsed.getFormattedText());
            } else {
                reply = replyGuide;
            }
        } catch (IllegalArgumentException e) {
            reply = replyGuide;

            response = null;
        }

        expenseService.sendReplyToUser(chatId, reply);
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
