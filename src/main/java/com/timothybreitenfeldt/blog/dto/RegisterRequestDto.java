package com.timothybreitenfeldt.blog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @Size(min = 4, max = 35)
    @NotNull(message = "Username cannot be null.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
    private String username;

    @Size(min = 7, max = 35, message = "Password must be between 7 and 35 characters.")
    @NotNull(message = "Password cannot be null.")
    private String password;

    @NotNull(message = "Email address cannot be null.")
    @Email(message = "Must be a valid email address.")
    private String email;

}
