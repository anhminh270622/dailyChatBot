package com.example.dailychatbot.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramUser {
    private long id;
    private String first_name;
    private String last_name;
    private String username;
    private boolean is_bot;
    private String language_code;
}
