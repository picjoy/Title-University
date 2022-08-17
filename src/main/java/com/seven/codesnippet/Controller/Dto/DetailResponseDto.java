package com.seven.codesnippet.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailResponseDto {

    private Long id;
    private String userNickname;
    private String title;
    private String imageUrl;
    private LocalDateTime createAt;
    private Long like_num;


}