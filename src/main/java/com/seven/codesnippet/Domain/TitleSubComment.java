package com.seven.codesnippet.Domain;

import com.seven.codesnippet.Request.ReCommentRequestDto;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
public class TitleSubComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private TitleComment comment;



    public boolean validateMember(Member member) {
        return !this.member.equals(member.getNickname());
    }

    public void update(ReCommentRequestDto requestDto) {
        this.content = requestDto.getContents();
    }
}
