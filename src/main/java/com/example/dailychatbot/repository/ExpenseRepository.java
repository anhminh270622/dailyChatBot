package com.example.dailychatbot.repository;

import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.entity.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByFrom_IdAndDateBetween(long chatId, Long startEpoch, Long endEpoch);
}
