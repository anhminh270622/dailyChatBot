package com.example.dailychatbot.service;

import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.response.ExpenseResponse;
import com.example.dailychatbot.entity.Expense;
import com.example.dailychatbot.mapper.ExpenseMapper;
import com.example.dailychatbot.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ExpenseService {
    ExpenseRepository expenseRepository;
    ExpenseMapper expenseMapper;

    public ExpenseResponse saveFromTelegram(TelegramMessage telegramMessage) {
        Expense expense = expenseMapper.fromTelegramMessage(telegramMessage);
        return expenseMapper.toExpenseResponse(expenseRepository.save(expense));
    }
    public List<ExpenseResponse> getAllExpenses() {
        return expenseMapper.toExpenseResponseList(expenseRepository.findAll());
    }
    public ExpenseResponse getExpenseById(String id) {
        return expenseMapper.toExpenseResponse(expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu với id: " + id)));
    }

}
