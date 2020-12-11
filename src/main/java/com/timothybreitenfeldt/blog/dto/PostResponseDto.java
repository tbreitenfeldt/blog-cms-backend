package com.timothybreitenfeldt.blog.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String username;

}
