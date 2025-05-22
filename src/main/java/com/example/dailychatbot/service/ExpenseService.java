package com.example.dailychatbot.service;

import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.response.ExpenseResponse;
import com.example.dailychatbot.entity.Expense;
import com.example.dailychatbot.mapper.ExpenseMapper;
import com.example.dailychatbot.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ExpenseService {
    ExpenseRepository expenseRepository;
    ExpenseMapper expenseMapper;

    public ExpenseResponse saveFromTelegram(TelegramMessage telegramMessage) {
        Expense expense = expenseMapper.toExpense(telegramMessage);
        if (expense == null) {
            throw new IllegalArgumentException("Expense entity must not be null");
        }
        return expenseMapper.toExpenseResponse(expenseRepository.save(expense));
    }


    public List<ExpenseResponse> getAllExpenses() {
        return expenseMapper.toExpenseResponseList(expenseRepository.findAll());
    }

    public ExpenseResponse getExpenseById(String id) {
        return expenseMapper.toExpenseResponse(expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu với id: " + id)));
    }

    @Autowired
    RestTemplate restTemplate;

//    @Value("${jwt.botToken}")
//    private String botToken;
//
//    private void sendReplyToUser(Long chatId, String text) {
//        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("chat_id", chatId);
//        requestBody.put("text", text);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//        try {
//            restTemplate.postForEntity(url, request, String.class);
//        } catch (Exception e) {
//            log.error("❌ Gửi phản hồi tới Telegram thất bại", e);
//        }
//    }

}
