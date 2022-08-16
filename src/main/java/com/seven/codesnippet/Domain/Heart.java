package com.seven.codesnippet.Domain;

import javax.persistence.*;

@Entity
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private TitlePost post;

}
