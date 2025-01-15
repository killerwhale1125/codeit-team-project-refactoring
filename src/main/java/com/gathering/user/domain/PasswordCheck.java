package com.gathering.user.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordCheck {

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[a-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase letter, one digit, one special character, and be at least 8 characters long"
    )
    @NotEmpty
    private String password;
}
