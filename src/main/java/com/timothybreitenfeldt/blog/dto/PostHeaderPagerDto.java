package com.timothybreitenfeldt.blog.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostHeaderPagerDto {

    private int pageNumber;
    private long pageSize;
    private int totalPages;
    private List<PostHeaderResponseDto> posts;

}
