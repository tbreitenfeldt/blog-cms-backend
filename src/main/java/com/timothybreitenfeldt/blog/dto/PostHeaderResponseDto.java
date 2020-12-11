package com.timothybreitenfeldt.blog.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostHeaderResponseDto {

    private Long id;
    private String title;
    private LocalDateTime updatedOn;
    private String username;

}
