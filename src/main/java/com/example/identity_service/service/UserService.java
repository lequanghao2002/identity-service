package com.example.identity_service.service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.enums.Role;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser((request));
        user.setPassword(passwordEncoder.encode((request.getPassword())));

//        var roles = roleRepository.findAllById(new List<String>("1"));
//        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return userRepository.save(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        log.warn(context.toString());
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')") // Kiểm tra trước khi vào method
    public List<UserResponse> getUsers() {
        return userMapper.toUsersResponse(userRepository.findAll());
    }

    //@PostAuthorize("hasRole('ADMIN')") // Kiểm tra sau khi vào method thực hiện
    @PostAuthorize("returnObject.username == authentication.name")
    // Nếu username trả về là username đăng nhập thì mới trả về
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(("User not found")));
    }

    public UserResponse updateUser(com.example.identity_service.dto.request.UserUpdateRequest request, String userId) {
        User user = getUserById(userId);

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
