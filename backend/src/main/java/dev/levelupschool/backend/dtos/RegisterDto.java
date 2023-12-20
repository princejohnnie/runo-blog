package dev.levelupschool.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    @NotBlank
    @Email
    private String email;
    @Size(min = 1, max = 256)
    private String name;
    @Size(min = 3, max = 25)
    private String password;
}
