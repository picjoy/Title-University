package com.seven.codesnippet.Controller.Dto;

import com.seven.codesnippet.Domain.TitlePost;
import lombok.Getter;

@Getter
public class TitlePostListDto {
    Long id;
    String title;
    String userNickname;
    String imageUrl;
    Long comment_num;
    Long like_num;

    public TitlePostListDto(TitlePost titlePost, Long aLong) {
        this.id = titlePost.getId();
        this.title = titlePost.getTitle();
        this.userNickname = titlePost.getMember().getNickname();
        this.imageUrl = titlePost.getImage();
        this.comment_num = aLong;
        if(titlePost.getHeart()==null){
            this.like_num = Long.valueOf(0);
        } else {
            this.like_num = titlePost.getHeart();
        }
    }
}
