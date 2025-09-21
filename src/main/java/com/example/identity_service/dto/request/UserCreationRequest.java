package com.example.identity_service.dto.request;

import com.example.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    @Size(min = 3, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;
}
