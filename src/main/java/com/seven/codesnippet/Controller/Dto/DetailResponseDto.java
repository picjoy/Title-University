package com.seven.codesnippet.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailResponseDto {

    private Long id;
    private String userNickname;
    private String title;
    private String imageUrl;
    private Long like_num;


}