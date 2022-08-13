package com.seven.codesnippet.Controller.Dto;

import com.seven.codesnippet.Domain.TitlePost;
import lombok.Getter;

@Getter
public class TitlePostListDto {
    Long id;
    String title;
    String userNickname;
    String imageUrl;
    Long like_num;

    public TitlePostListDto(TitlePost titlePost) {
        this.id = titlePost.getId();
        this.title = titlePost.getTitle();
        this.userNickname = titlePost.getMember().getNickname();
        this.imageUrl = titlePost.getImage();
        this.like_num = titlePost.getHeart();
    }
}
