package com.seven.codesnippet.Controller.Dto;


import com.seven.codesnippet.Domain.TitlePost;
import lombok.Getter;

@Getter
public class PostOwnerDto {
    Long id;
    String title;
    String userNickname;
    String imageUrl;
    Long like_num;
    boolean userlike;
    boolean postowner;

    public PostOwnerDto(TitlePost titlePost, boolean bo, boolean boo) {
        this.id = titlePost.getId();
        this.title = titlePost.getTitle();
        this.userNickname = titlePost.getMember().getNickname();
        this.imageUrl = titlePost.getImage();
        this.like_num = titlePost.getHeart();
        this.userlike = bo;
        this.postowner = boo;
    }
}

