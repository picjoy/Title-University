package com.seven.codesnippet.Domain;

import javax.persistence.*;

@Entity
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String Auther;

    @Column(nullable = false)
    private Long postid;
}
