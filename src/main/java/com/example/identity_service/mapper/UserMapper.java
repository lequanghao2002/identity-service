package com.example.identity_service.mapper;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    List<UserResponse> toUsersResponse(List<User> users);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, com.example.identity_service.dto.request.UserUpdateRequest request);
}
