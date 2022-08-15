package com.seven.codesnippet.Controller.Dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TitlePostListListDto {
    List<TitlePostListDto> titlePostListDtos;

    public TitlePostListListDto(List<TitlePostListDto> titlePostListDtos) {
        this.titlePostListDtos = titlePostListDtos;
    }
}
