package com.example.dailychatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseResponse {
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
