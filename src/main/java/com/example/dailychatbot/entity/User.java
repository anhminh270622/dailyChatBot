package com.example.dailychatbot.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "user")
public class User {
    String id;
    String first_name;
    String last_name;
    String username;
    String is_bot;
}
