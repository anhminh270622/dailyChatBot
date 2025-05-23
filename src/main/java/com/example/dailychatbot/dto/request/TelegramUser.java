package com.example.dailychatbot.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramUser {
    long id;
    String first_name;
    String last_name;
    String username;
    boolean is_bot;
    String language_code;
}
