package com.example.dailychatbot.entity;

import com.example.dailychatbot.dto.request.TelegramChat;
import com.example.dailychatbot.dto.request.TelegramMessage;
import com.example.dailychatbot.dto.request.TelegramUser;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "expense")
public class Expense {
    @Id
    String id;
    int message_id;
    TelegramUser from;
    TelegramChat chat;
    long date;
    String text;
}
