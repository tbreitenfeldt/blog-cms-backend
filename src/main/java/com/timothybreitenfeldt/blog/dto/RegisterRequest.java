package com.timothybreitenfeldt.blog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 4, max = 35)
    @NotNull(message = "Username cannot be null.")
    private String username;

    @Size(min = 7, max = 35, message = "Password must be between 7 and 35 characters.")
    @NotNull(message = "Password cannot be null.")
    private String password;

    @NotNull(message = "Email address cannot be null.")
    @Email
    private String email;

}
