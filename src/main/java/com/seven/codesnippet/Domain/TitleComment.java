package com.seven.codesnippet.Domain;

import com.seven.codesnippet.Request.CommentPutRequestDto;
import com.seven.codesnippet.Request.CommentRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
public class TitleComment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String content;

    @Column (nullable = false)
    private String member;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TitleSubComment> titlecomments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private TitlePost post;

    public boolean validateMember(Member member) {
        return !this.member.equals(member.getNickname());
    }
    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
    public void update(CommentPutRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContents();
    }

}
