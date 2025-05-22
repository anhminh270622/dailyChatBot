package com.example.dailychatbot.service;

import com.example.dailychatbot.dto.request.TelegramUser;
import com.example.dailychatbot.dto.response.UserResponse;
import com.example.dailychatbot.entity.Expense;
import com.example.dailychatbot.mapper.UserMapper;
import com.example.dailychatbot.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

public class UserService {
    ExpenseRepository expenseRepository;
    UserMapper userMapper;

    public List<UserResponse> getAllUsers() {
        List<Expense> expenses = expenseRepository.findAll();
        List<TelegramUser> telegramUsers = new ArrayList<>(expenses.stream()
                .map(Expense::getFrom)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(
                        TelegramUser::getId,
                        user -> user,
                        (existing, replacement) -> existing
                ))
                .values());
        return userMapper.toUserResponseList(telegramUsers);
    }

    public UserResponse getUserById(long id) {
        List<Expense> expenses = expenseRepository.findAll();

        TelegramUser telegramUser = expenses.stream()
                .map(Expense::getFrom)
                .filter(Objects::nonNull)
                .filter(user -> user.getId() == id) // So sánh trực tiếp long
                .findFirst()
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy người dùng với id: " + id));

        return userMapper.toUserResponse(telegramUser);
    }

}
