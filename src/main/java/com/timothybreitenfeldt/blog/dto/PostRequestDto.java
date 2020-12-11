package com.timothybreitenfeldt.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9 ,._\\-'\"!#]+$")
    private String title;

    @NotBlank(message = "Content cannot be blank.")
    private String content;

}
