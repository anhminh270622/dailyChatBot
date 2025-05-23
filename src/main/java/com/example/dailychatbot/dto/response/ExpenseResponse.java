package com.example.dailychatbot.dto.response;

import com.example.dailychatbot.dto.request.TelegramChat;
import com.example.dailychatbot.dto.request.TelegramUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseResponse {
    String id;
    int message_id;
    TelegramUser from;
    TelegramChat chat;
    long date;
    String text;
}
