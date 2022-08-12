package com.seven.codesnippet.Domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String nickname;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TitlePost> postList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TitleComment> commentList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TitleSubComment> reCommentList;

}
