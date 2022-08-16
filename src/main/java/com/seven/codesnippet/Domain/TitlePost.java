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
public class TitlePost extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String image;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TitleComment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = true)
    private Long heart;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> heartList;

    public void updateHeart(Long heart){
        this.heart = heart;
    }

    public TitlePost(String title, Member member, Long heart,String image) {
        this.title = title;
        this.member = member;
        this.image = image;
        this.heart = heart;
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

}
