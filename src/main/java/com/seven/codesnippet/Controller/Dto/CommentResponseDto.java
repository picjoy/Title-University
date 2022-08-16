package com.seven.codesnippet.Controller.Dto;

import com.seven.codesnippet.Domain.TitleComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto{
    private Long id;
    private String userCommented;
    private String comment;
    private Long postId;
    private boolean commentOwner;


}
