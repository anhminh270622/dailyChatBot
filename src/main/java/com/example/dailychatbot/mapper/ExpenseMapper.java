package com.example.dailychatbot.mapper;

import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.response.ExpenseResponse;
import com.example.dailychatbot.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    Expense toExpense(TelegramMessage telegramMessage);

    @Named("longToDate")
    static Date longToDate(long timestamp) {
        return new Date(timestamp * 1000); // Nếu timestamp là giây (như của Telegram), cần * 1000
    }

    ExpenseResponse toExpenseResponse(Expense expense);

    List<ExpenseResponse> toExpenseResponseList(List<Expense> expenses);

}



