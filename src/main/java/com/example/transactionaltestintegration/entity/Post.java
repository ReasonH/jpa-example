package com.example.transactionaltestintegration.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Category category;

    @OneToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    public Post(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getId() + " " + getTitle();
    }
}
