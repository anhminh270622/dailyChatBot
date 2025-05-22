package com.example.dailychatbot.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpenseRequest {
    @Id
    String id;
    String message_id;
    String first_name;
    String last_name;
    String username;
    Long userId;
    String text;
    String type;
    Date date;
}
