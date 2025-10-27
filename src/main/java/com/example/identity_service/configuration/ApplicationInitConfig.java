package com.example.identity_service.configuration;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.identity_service.entity.Permission;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.repository.PermissionRepository;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        return args -> {
            // === 1. Khởi tạo Permission ===
            List<String> permissionNames = List.of("CREATOR", "APPROVER", "VIEWER");
            List<Permission> permissionsToSave = permissionNames.stream()
                    .filter(name -> !permissionRepository.existsByName(name))
                    .map(name -> Permission.builder().name(name).build())
                    .toList();
            if (!permissionsToSave.isEmpty()) {
                permissionRepository.saveAll(permissionsToSave);
            }

            List<Permission> allPermissions = permissionRepository.findAll();
            Permission viewerPermission = permissionRepository
                    .findByName("VIEWER")
                    .orElseThrow(() -> new RuntimeException("VIEWER permission not found"));

            // === 2. Khởi tạo Role ===
            if (!roleRepository.existsByName("ADMIN")) {
                Role adminRole = Role.builder()
                        .name("ADMIN")
                        .permissions(new HashSet<>(allPermissions)) // ADMIN có hết quyền
                        .build();
                roleRepository.save(adminRole);
            }
            if (!roleRepository.existsByName("USER")) {
                Role userRole = Role.builder()
                        .name("USER")
                        .permissions(Set.of(viewerPermission)) // USER chỉ có VIEWER
                        .build();
                roleRepository.save(userRole);
            }

            // === 3. Khởi tạo User admin ===
            if (!userRepository.existsByUsername("admin")) {
                Role adminRole = roleRepository
                        .findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123"))
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(admin);
                log.warn("Admin user has been created with username=admin, password=123");
            }

            if (!userRepository.existsByUsername("haolq")) {
                Role userRole = roleRepository
                        .findByName("USER")
                        .orElseThrow(() -> new RuntimeException("Role USER not found"));

                User normalUser = User.builder()
                        .username("haolq")
                        .lastName("Hào")
                        .firstName("Lê")
                        .dob(LocalDate.of(2002, 12, 15))
                        .password(passwordEncoder.encode("123"))
                        .roles(Set.of(userRole))
                        .build();

                userRepository.save(normalUser);
                log.warn("Normal  user has been created with username=admin, password=123");
            }
        };
    }
}
