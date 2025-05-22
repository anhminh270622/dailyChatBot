package com.example.dailychatbot.entity;

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
    String message_id;
    String first_name;
    String last_name;
    String username;
    Long userId;
    String text;
    String type;
    Date date;
}
