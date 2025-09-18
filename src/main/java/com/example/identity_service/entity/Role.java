package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;

    @ManyToMany
    Set<Permission> permissions;
}
