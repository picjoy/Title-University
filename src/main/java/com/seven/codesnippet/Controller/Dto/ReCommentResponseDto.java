package com.seven.codesnippet.Controller.Dto;

import com.seven.codesnippet.Domain.TitleSubComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReCommentResponseDto {
        private Long id;
        private String userSubCommented;
        private String subComment;
        private Long commentId;
        private Long postId;

        public ReCommentResponseDto(TitleSubComment entity) {
                this.id = entity.getId();
                this.userSubCommented = entity.getMember();
                this.subComment = entity.getContent();
                this.postId = entity.getComment().getPost().getId();
                this.commentId = entity.getComment().getId();
        }

}
