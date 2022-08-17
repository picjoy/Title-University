package com.seven.codesnippet.Controller.Dto;


import com.seven.codesnippet.Domain.TitlePost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostOwnerDto {
    Long id;
    String title;
    String userNickname;
    String imageUrl;
    LocalDateTime createAt;
    Long like_num;
    boolean userlike;
    boolean postowner;

    public PostOwnerDto(TitlePost titlePost, boolean bo, boolean boo) {
        this.id = titlePost.getId();
        this.title = titlePost.getTitle();
        this.userNickname = titlePost.getMember().getNickname();
        this.imageUrl = titlePost.getImage();
        this.createAt = titlePost.getCreatedAt();
        this.userlike = bo;
        this.postowner = boo;
        if(titlePost.getHeart()==null){
            this.like_num = Long.valueOf(0);
        } else {
            this.like_num = titlePost.getHeart();
        }
    }
}

