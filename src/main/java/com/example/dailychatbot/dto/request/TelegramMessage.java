package com.example.dailychatbot.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramMessage {
    private int message_id;
    private TelegramUser from;
    private TelegramChat chat;
    private long date;
    private String text;
}
