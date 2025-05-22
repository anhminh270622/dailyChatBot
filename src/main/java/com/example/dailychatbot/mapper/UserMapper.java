package com.example.dailychatbot.mapper;

import com.example.dailychatbot.dto.request.TelegramUser;
import com.example.dailychatbot.dto.response.UserResponse;
import com.example.dailychatbot.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(TelegramUser user);
    User toUser(UserResponse userResponse);

    List<UserResponse> toUserResponseList(List<TelegramUser> users);
}
