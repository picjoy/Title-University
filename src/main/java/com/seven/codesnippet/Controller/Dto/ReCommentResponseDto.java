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
        private String userCommented;
        private String contents;
        private Long postId;
        private Long commentId;

        public ReCommentResponseDto(TitleSubComment entity) {
                this.id = entity.getId();
                this.userCommented = entity.getMember();
                this.contents = entity.getContent();
                this.postId = entity.getComment().getPost().getId();
                this.commentId = entity.getComment().getId();
        }

}
