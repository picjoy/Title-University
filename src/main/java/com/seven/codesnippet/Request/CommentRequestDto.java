package com.seven.codesnippet.Request;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
public class CommentRequestDto {
    Long postId;
    String content;
}
