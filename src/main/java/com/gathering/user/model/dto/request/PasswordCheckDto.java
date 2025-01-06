package com.gathering.user.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordCheckDto {

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[a-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase letter, one digit, one special character, and be at least 8 characters long"
    )
    @NotEmpty
    private String password;
}
