package com.example.dailychatbot.mapper;

import com.example.dailychatbot.dto.request.ExpenseRequest;
import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.response.ExpenseResponse;
import com.example.dailychatbot.entity.Expense;
import org.mapstruct.Mapper;

import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    Expense toExpense(ExpenseRequest expenseRequest);

    ExpenseResponse toExpenseResponse(Expense expense);
    List<ExpenseResponse> toExpenseResponseList(List<Expense> expenses);
    // Sửa dòng này
    default Expense fromTelegramMessage(TelegramMessage telegramMessage) {
        if (telegramMessage == null || telegramMessage.getFrom() == null || telegramMessage.getChat() == null) {
            return null;
        }

        Expense expense = new Expense();
        expense.setMessage_id(String.valueOf(telegramMessage.getMessage_id()));
        expense.setFirst_name(telegramMessage.getFrom().getFirst_name());
        expense.setLast_name(telegramMessage.getFrom().getLast_name());
        expense.setUsername(telegramMessage.getFrom().getUsername());
        expense.setUserId(telegramMessage.getFrom().getId());
        expense.setText(telegramMessage.getText());
        expense.setType(telegramMessage.getChat().getType());
        expense.setDate(new Date(telegramMessage.getDate() * 1000L));
        return expense;
    }
}



